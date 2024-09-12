package org.abondar.experimental.sagademo.payment

import org.abondar.experimental.sagademo.message.PaymentMessage
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
import org.mockito.Mock
import org.mockito.Mockito.*
import java.math.BigDecimal

@MockEndpoints
@CamelSpringBootTest
@EnableRouteCoverage
class PaymentRouteTest : CamelTestSupport() {

    @Mock
    lateinit var paymentDao: PaymentDao

    override fun createRouteBuilder(): RouteBuilder {

        val paymentProcessor = PaymentProcessor()
        return PaymentRoute(paymentDao, paymentProcessor)
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
    fun `test process payment route`() {
        val paymentMessage = PaymentMessage(BigDecimal(23.44), "EUR", "CARD")
        val payment = Payment(
            orderId = "test",
            sum = paymentMessage.sum,
            currency = paymentMessage.currency,
            paymentType = paymentMessage.paymentType
        )


        AdviceWith.adviceWith(context, "paymentRoute") {
            it.replaceFromWith("direct:processPayment")
        }

        `when`(paymentDao.save(payment)).thenReturn(payment)

        template.sendBodyAndHeaders(
            "direct:processPayment", null, mapOf(
                "PaymentMessage" to paymentMessage,
                "OrderId" to "test"
            )
        )

        verify(paymentDao, times(1)).save(payment)


    }


    @Test
    fun `test cancel payment route`() {
        AdviceWith.adviceWith(context, "cancelPaymentRoute") {
            it.replaceFromWith("direct:cancelPayment")
        }

        doNothing().`when`(paymentDao).deleteByOrderId(anyString())

        template.sendBodyAndHeader("direct:cancelPayment", null, "OrderId", "test")

        verify(paymentDao, times(1)).deleteByOrderId(anyString())
    }
}