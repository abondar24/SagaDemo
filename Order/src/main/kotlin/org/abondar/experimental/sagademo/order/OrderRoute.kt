package org.abondar.experimental.sagademo.order

import org.apache.camel.builder.RouteBuilder
import org.springframework.stereotype.Component

@Component
class OrderRoute(
    private val orderDao: OrderDao,
    private val orderProcessor: OrderProcessor
) : RouteBuilder() {

    override fun configure() {

        onException(Exception::class.java)
            .handled(true)
            .log("Exception occured : ${body()}")

        from("jms:queue:createOrder")
            .routeId("orderRoute")
            .process(orderProcessor)
            .bean(orderDao, "save")
            .log("Order saved successfully")
            .end()


        from("jms:queue:cancelOrder")
            .routeId("cancelOrderRoute")
            .bean(orderDao, "deleteByOrderId(\${header.orderId})")
            .log("Order cancelled")
            .end()
    }
}