package org.abondar.experimental.sagademo.payment

import org.abondar.experimental.sagademo.message.PaymentMessage
import org.apache.camel.builder.RouteBuilder
import org.springframework.stereotype.Component

@Component
class PaymentRoute(private val paymentDao: PaymentDao) : RouteBuilder() {


    override fun configure() {

        onException(Exception::class.java)
            .handled(true)
            .log("Exception occurred: \${exception.message}")
            .setHeader("orderId", simple("\${body.orderId}"))
            .to("jms:queue:notifyOrderCancellation")

        from("jms:queue:processPayment")
            .routeId("paymentRoute")
            .process { exchange ->

                val paymentRequest = exchange.getIn().getBody(PaymentMessage::class.java)
                val payment = Payment(
                    orderId = paymentRequest.orderId,
                    sum = paymentRequest.sum,
                    currency = paymentRequest.currency,
                    paymentType = paymentRequest.paymentType
                )

                exchange.getIn().body = payment
            }
            .bean(paymentDao,"save")
            .log("Payment Processed successfully")


        from("jms:queue:cancelPayment")
            .routeId("cancelPaymentRoute")
            .log("Cancel Payment")
            .bean(paymentDao, "deleteByOrderId")
            .log("Payment Cancelled")
            .to("jms:queue:notifyOrderCancellation")
    }

}