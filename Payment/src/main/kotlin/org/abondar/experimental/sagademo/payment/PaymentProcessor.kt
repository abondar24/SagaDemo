package org.abondar.experimental.sagademo.payment

import org.abondar.experimental.sagademo.message.PaymentMessage
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.springframework.stereotype.Component

@Component
class PaymentProcessor : Processor {
    override fun process(exchange: Exchange?) {
        val paymentRequest = exchange?.getIn()?.getHeader("PaymentMessage", PaymentMessage::class.java)
        val orderId = exchange?.getIn()?.getHeader("OrderId", String::class.java)

        paymentRequest?.let {
            orderId?.let {
                val payment = Payment(
                    orderId = orderId,
                    sum = paymentRequest.sum,
                    currency = paymentRequest.currency,
                    paymentType = paymentRequest.paymentType
                )

                exchange.getIn().body = payment
            } ?: throw IllegalArgumentException("Order id missing")
        } ?: throw IllegalArgumentException("Order message is missing")


    }
}