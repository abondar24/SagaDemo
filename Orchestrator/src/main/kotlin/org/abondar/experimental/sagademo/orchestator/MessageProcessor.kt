package org.abondar.experimental.sagademo.orchestator

import com.fasterxml.jackson.databind.ObjectMapper
import org.abondar.experimental.sagademo.message.InventoryMessage
import org.abondar.experimental.sagademo.message.OrderCreateRequest
import org.abondar.experimental.sagademo.message.OrderMessage
import org.abondar.experimental.sagademo.message.PaymentMessage
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.springframework.stereotype.Component

@Component
class MessageProcessor(private val objectMapper: ObjectMapper) : Processor {
    override fun process(exchange: Exchange?) {

        exchange?.getIn()?.getBody(String::class.java)?.let { json ->

            val request = objectMapper.readValue(json, OrderCreateRequest::class.java)

            val paymentMessage = PaymentMessage(
                sum = request.sum,
                currency = request.currency,
                paymentType = request.paymentType
            )

            exchange.getIn().setHeader("PaymentMessage", paymentMessage)

            val orderMessage = OrderMessage(
                recipientName = request.recipientName,
                recipientLastName = request.recipientLastName,
                shippingAddress = request.shippingAddress,
            )
            exchange.getIn().setHeader("OrderMessage", orderMessage)

            val inventoryMessage = InventoryMessage(
                items = request.items
            )
            exchange.getIn().setHeader("InventoryMessage", inventoryMessage)
        } ?: {
            exchange?.setProperty(Exchange.EXCEPTION_CAUGHT, IllegalArgumentException("OrderCreateRequest is null"))
            exchange?.getIn()?.setBody(null)
        }

    }
}