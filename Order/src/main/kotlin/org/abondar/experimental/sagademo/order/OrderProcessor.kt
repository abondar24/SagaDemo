package org.abondar.experimental.sagademo.order

import com.fasterxml.jackson.databind.ObjectMapper
import org.abondar.experimental.sagademo.message.OrderMessage
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.springframework.stereotype.Component

@Component
class OrderProcessor(private val objectMapper: ObjectMapper) : Processor {
    override fun process(exchange: Exchange?) {
        val request = exchange?.getIn()?.getBody(String::class.java)
        val orderId = exchange?.getIn()?.getHeader("OrderId", String::class.java)

        request?.let {
            val orderMessage = objectMapper.readValue(request, OrderMessage::class.java)
            orderId?.let { id ->
                val order = Order(
                    orderId = id,
                    shippingAddress = orderMessage.shippingAddress,
                    recipientName = orderMessage.recipientName,
                    recipientLastName = orderMessage.recipientLastName
                )

                exchange.getIn().body = order
            } ?: throw IllegalArgumentException("Order id missing")

        } ?: throw IllegalArgumentException("Order message is missing")
    }
}