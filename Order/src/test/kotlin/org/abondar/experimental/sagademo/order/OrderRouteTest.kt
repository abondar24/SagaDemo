package org.abondar.experimental.sagademo.order

import org.abondar.experimental.sagademo.message.OrderMessage
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
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*


@MockEndpoints
@CamelSpringBootTest
@EnableRouteCoverage
class OrderRouteTest : CamelTestSupport() {

    @Mock
    lateinit var orderDao: OrderDao

    @InjectMocks
    lateinit var orderRoute: OrderRoute


    override fun createRouteBuilder(): RouteBuilder {
        return orderRoute
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
    fun `test create order route`(){
        val orderMessage = OrderMessage("test","test","test","test")
        val order = Order(
            orderId = orderMessage.orderId,
            shippingAddress = orderMessage.shippingAddress,
            recipientName = orderMessage.recipientName,
            recipientLastName = orderMessage.recipientLastName,
        )

        AdviceWith.adviceWith(context,"orderRoute") {
            it.replaceFromWith("direct:createOrder")
        }

        `when`(orderDao.save(order)).thenReturn(order)

        template.sendBody("direct:createOrder",orderMessage)

        verify(orderDao, times(1)).save(order)

    }

    @Test
    fun `test cancel order route`(){
        val orderMessage = OrderMessage("test","test","test","test")

        val mockEndpoint  = getMockEndpoint("mock:jms:queue:notifyOrderCancellation")

        AdviceWith.adviceWith(context,"cancelOrderRoute") {
            it.replaceFromWith("direct:cancelOrder")

            it.interceptSendToEndpoint("jms:queue:notifyOrderCancellation")
                .skipSendToOriginalEndpoint()
                .to(mockEndpoint.endpointUri)


        }

        doNothing().`when`(orderDao).deleteByOrderId(anyString())

        template.sendBody("direct:cancelOrder",orderMessage)

        verify(orderDao, times(1)).deleteByOrderId(anyString())

       mockEndpoint.apply {
            expectedMessageCount(1)
            assertIsSatisfied()
        }


    }


}