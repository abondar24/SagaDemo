package org.abondar.experimental.sagademo.payment

import org.apache.camel.builder.RouteBuilder
import org.springframework.stereotype.Component

@Component
class PaymentRoute(
    private val paymentDao: PaymentDao,
    private val paymentProcessor: PaymentProcessor
) : RouteBuilder() {


    override fun configure() {

        onException(Exception::class.java)
            .handled(true)
            .log("Exception occurred : ${body()}")

        from("jms:queue:processPayment")
            .routeId("paymentRoute")
            .process(paymentProcessor)
            .bean(paymentDao, "save")
            .log("Payment Processed successfully")
            .end()


        from("jms:queue:cancelPayment")
            .routeId("cancelPaymentRoute")
            .log("Cancel Payment")
            .bean(paymentDao, "deleteByOrderId(\${header.orderId})")
            .log("Payment Cancelled")
            .end()
    }

}