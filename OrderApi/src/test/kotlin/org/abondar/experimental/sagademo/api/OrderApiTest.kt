package org.abondar.experimental.sagademo.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.abondar.experimental.sagademo.message.ItemMessage
import org.abondar.experimental.sagademo.message.OrderCreateRequest
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory
import org.apache.camel.builder.AdviceWith
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.component.jms.JmsComponent
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.test.junit5.CamelTestSupport
import org.apache.camel.test.spring.junit5.CamelSpringBootTest
import org.junit.jupiter.api.Test
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import java.math.BigDecimal

@SpringJUnitConfig
@CamelSpringBootTest
class OrderApiTest : CamelTestSupport() {


    override fun createRouteBuilder(): RouteBuilder {
        return OrderApiRoute()
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
    fun `post order test`() {
        val objectMapper = ObjectMapper()

        val request = OrderCreateRequest(
            recipientName = "test",
            recipientLastName = "test",
            shippingAddress = "test",
            sum = BigDecimal(23.44),
            currency = "EUR",
            paymentType = "CARD",
            items = listOf(ItemMessage("test", 3))
        )

        val jsonRequest = objectMapper.writeValueAsString(request)

        val mockEndpoint = getMockEndpoint("mock:jms:queue:startOrderProcessing")

        AdviceWith.adviceWith(context, "orderPost") {
            it.replaceFromWith("direct:orderPost")

            it.interceptSendToEndpoint("jms:queue:startOrderProcessing")
                .skipSendToOriginalEndpoint()
                .to(mockEndpoint.endpointUri)

        }


        template.sendBody("direct:orderPost", jsonRequest)

        mockEndpoint.apply {
            expectedMessageCount(1)
            assertIsSatisfied()
        }

    }


    @Test
    fun `delete order test`() {
        val mockEndpoint = getMockEndpoint("mock:jms:queue:cancelOrderProcessing")

        AdviceWith.adviceWith(context, "orderDelete") {
            it.replaceFromWith("direct:orderDelete")

            it.interceptSendToEndpoint("jms:queue:cancelOrderProcessing")
                .skipSendToOriginalEndpoint()
                .to(mockEndpoint.endpointUri)

        }


        template.sendBodyAndHeader("direct:orderDelete", null,"orderId","test")

        mockEndpoint.apply {
            expectedMessageCount(1)
            assertIsSatisfied()
        }

    }
}