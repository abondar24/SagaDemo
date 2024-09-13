package org.abondar.experimental.sagademo.payment

import com.fasterxml.jackson.databind.ObjectMapper
import org.abondar.experimental.sagademo.message.PaymentMessage
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.springframework.stereotype.Component

@Component
class PaymentProcessor(private val objectMapper: ObjectMapper) : Processor {
    override fun process(exchange: Exchange?) {
        val request = exchange?.getIn()?.getBody(String::class.java)
        val orderId = exchange?.getIn()?.getHeader("OrderId", String::class.java)

        request?.let {
            val paymentMessage = objectMapper.readValue(request, PaymentMessage::class.java)
            orderId?.let {
                val payment = Payment(
                    orderId = orderId,
                    sum = paymentMessage.sum,
                    currency = paymentMessage.currency,
                    paymentType = paymentMessage.paymentType
                )

                exchange.getIn().body = payment
            } ?: throw IllegalArgumentException("Order id missing")
        } ?: throw IllegalArgumentException("Order message is missing")


    }
}