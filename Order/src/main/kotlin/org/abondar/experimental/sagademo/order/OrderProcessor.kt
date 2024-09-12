package org.abondar.experimental.sagademo.order

import org.abondar.experimental.sagademo.message.OrderMessage
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.springframework.stereotype.Component

@Component
class OrderProcessor : Processor {
    override fun process(exchange: Exchange?) {
        val orderRequest = exchange?.getIn()?.getHeader("OrderMessage", OrderMessage::class.java)
        val orderId = exchange?.getIn()?.getHeader("OrderId",String::class.java)

        orderRequest?.let {
            orderId?.let { id ->
                val order = Order(
                    orderId = id,
                    shippingAddress = orderRequest.shippingAddress,
                    recipientName = orderRequest.recipientName,
                    recipientLastName = orderRequest.recipientLastName
                )

                exchange.getIn().body = order
            } ?: throw IllegalArgumentException("Order id missing")
        } ?: throw IllegalArgumentException("Order message is missing")

    }
}