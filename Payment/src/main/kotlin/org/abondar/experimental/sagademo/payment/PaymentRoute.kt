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
            .log("Exception occurred : \${exception.message}")

        from("jms:queue:processPayment")
            .routeId("paymentRoute")
            .process(paymentProcessor)
            .bean(paymentDao, "save")
            .log("Payment Processed successfully for order \${header.orderId}")
            .end()


        from("jms:queue:cancelPayment")
            .routeId("cancelPaymentRoute")
            .bean(paymentDao, "deleteByOrderId(\${header.orderId})")
            .log("Payment Cancelled for order \${header.orderId}")
            .end()
    }

}