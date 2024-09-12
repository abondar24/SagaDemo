package org.abondar.experimental.sagademo.orchestator

import org.abondar.experimental.sagademo.message.InventoryMessage
import org.abondar.experimental.sagademo.message.OrderCreateRequest
import org.abondar.experimental.sagademo.message.OrderMessage
import org.abondar.experimental.sagademo.message.PaymentMessage
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.springframework.stereotype.Component
import java.util.*

@Component
class MessageProcessor : Processor {
    override fun process(exchange: Exchange?) {

        val orderId = UUID.randomUUID().toString()
        exchange?.getIn()?.setHeader("OrderId", orderId)

        exchange?.getIn()?.getBody(OrderCreateRequest::class.java)?.let { request ->
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
        }

    }
}