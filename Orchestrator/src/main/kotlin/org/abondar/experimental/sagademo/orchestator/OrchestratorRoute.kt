package org.abondar.experimental.sagademo.orchestator

import org.apache.camel.builder.RouteBuilder
import org.springframework.stereotype.Component


@Component
class OrchestratorRoute(private val messageProcessor: MessageProcessor) : RouteBuilder() {
    override fun configure() {
        onException(Exception::class.java)
            .handled(true)
            .log("Exception occurred: \${exception.message}")
            .to("jms:queue:notifyOrderCancellation")

        from("jms:queue:startOrderProcessing")
            .routeId("orderProcessingRoute")
            .process(messageProcessor)
            .saga()
            .compensation("direct:cancelOrder")
            .log("Start order processing")

            .setBody().simple(null)  // Clear the body
            .setHeader("OrderMessage").simple("\${header.OrderMessage}")
            .setHeader("OrderId").simple("\${header.OrderId}")
            .to("jms:queue:createOrder")

            .setBody().simple(null)  // Clear the body
            .setHeader("PaymentMessage").simple("\${header.PaymentMessage}")
            .setHeader("OrderId").simple("\${header.OrderId}")
            .to("jms:queue:processPayment")

            .setBody().simple(null)  // Clear the body
            .setHeader("InventoryMessage").simple("\${header.InventoryMessage}")
            .setHeader("OrderId").simple("\${header.OrderId}")
            .to("jms:queue:processInventory")


            .completion("jms:queue:notifyOrderCompletion")
            .end()

        from("direct:cancelOrder")
            .setBody().simple(null)
            .setHeader("OrderId").simple("\${header.OrderId}")
            .to("jms:queue:cancelOrder")
            .to("jms:queue:cancelPayment")
            .to("jms:queue:revertInventory")
            .to("jms:queue:notifyOrderCancellation")

    }
}