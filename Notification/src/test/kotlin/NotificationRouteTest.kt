import org.abondar.experimental.sagademo.notification.NotificationRoute
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory
import org.apache.camel.builder.AdviceWith
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.component.jms.JmsComponent
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.test.junit5.CamelTestSupport
import org.apache.camel.test.spring.junit5.CamelSpringBootTest
import org.apache.camel.test.spring.junit5.EnableRouteCoverage
import org.apache.camel.test.spring.junit5.MockEndpoints
import org.junit.jupiter.api.Test

@MockEndpoints
@CamelSpringBootTest
@EnableRouteCoverage
class NotificationRouteTest : CamelTestSupport() {

    override fun createRouteBuilder(): RouteBuilder {
        return NotificationRoute()
    }

    override fun createCamelContext(): DefaultCamelContext {
        val context = DefaultCamelContext()

        val connectionFactory = ActiveMQConnectionFactory("vm://localhost?broker.persistent=false")
        val activeMQComponent = JmsComponent()
        activeMQComponent.connectionFactory = connectionFactory

        context.addComponent("jms", activeMQComponent)

        return context
    }

    @Test
    fun `test completion route`() {
        val body = "Order123"

        val mockEndpoint = getMockEndpoint("mock:notifyOrderCompletion")

        AdviceWith.adviceWith(context, "completionRoute") {
            it.interceptSendToEndpoint("jms:queue:notifyOrderCompletion")
                .skipSendToOriginalEndpoint()
                .to("mock:notifyOrderCompletion")
        }

        template.sendBody("jms:queue:notifyOrderCompletion", body)

        mockEndpoint.apply {
            expectedBodiesReceived(body)
            expectedMessageCount(1)
            assertIsSatisfied()
        }

    }


    @Test
    fun `test cancellation route`() {
        val body = "Order123"

        val mockEndpoint = getMockEndpoint("mock:notifyOrderCancellation")

        AdviceWith.adviceWith(context, "cancellationRoute") {
            it.interceptSendToEndpoint("jms:queue:notifyOrderCancellation")
                .skipSendToOriginalEndpoint()
                .to("mock:notifyOrderCancellation")
        }

        template.sendBody("jms:queue:notifyOrderCancellation", body)

        mockEndpoint.apply {
            expectedBodiesReceived(body)
            expectedMessageCount(1)
            assertIsSatisfied()
        }

    }
}