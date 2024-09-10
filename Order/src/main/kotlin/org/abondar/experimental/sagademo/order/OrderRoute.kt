package org.abondar.experimental.sagademo.order

import org.abondar.experimental.sagademo.message.OrderMessage
import org.apache.camel.builder.RouteBuilder

import org.springframework.stereotype.Component

@Component
class OrderRoute(private val orderDao: OrderDao) : RouteBuilder() {

    override fun configure() {

        onException(Exception::class.java)
            .handled(true)
            .to("jms:queue:notifyOrderCancellation")

        from("jms:queue:createOrder")
            .routeId("orderRoute")
            .process { exchange ->

                val orderRequest = exchange.getIn().getBody(OrderMessage::class.java)
                val order = Order(
                    orderId = orderRequest.orderId,
                    shippingAddress = orderRequest.shippingAddress,
                    recipientName = orderRequest.recipientName,
                    recipientLastName = orderRequest.recipientLastName
                )

                exchange.getIn().body = order
            }
            .bean(orderDao, "save")
            .log("Order saved successfully")


        from("jms:queue:cancelOrder")
            .routeId("cancelOrderRoute")
            .bean(orderDao, "deleteByOrderId")
            .log("Order cancelled")
            .to("jms:queue:notifyOrderCancellation")
    }
}