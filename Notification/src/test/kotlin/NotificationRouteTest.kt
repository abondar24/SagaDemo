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
import org.mockito.Mockito

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
        AdviceWith.adviceWith(context, "completionRoute") {
            it.replaceFromWith("direct:notifyOrderCompletion")
        }

        template.sendBody("direct:notifyOrderCompletion", "Order123")

        getMockEndpoint("mock:seda:camel").expectedMessageCount(1);

    }


    @Test
    fun `test cancellation route`() {
        AdviceWith.adviceWith(context, "cancellationRoute") {
            it.replaceFromWith("direct:notifyOrderCancellation")
        }

        template.sendBody("mock:notifyOrderCancellation", "Order123")

        getMockEndpoint("mock:seda:camel").expectedMessageCount(1);

    }
}