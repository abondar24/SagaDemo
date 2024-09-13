package org.abondar.experimental.sagademo.orchestator

import org.apache.camel.CamelContext
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.SagaPropagation
import org.apache.camel.saga.InMemorySagaService
import org.springframework.stereotype.Component
import java.util.*


@Component
class OrchestratorRoute(private val messageProcessor: MessageProcessor, private val camelContext: CamelContext) :
    RouteBuilder() {
    override fun configure() {
        camelContext.addService(InMemorySagaService())

        onException(Exception::class.java)
            .handled(true)
            .log("Exception occurred : \${exception.message}")
            .to("direct:cancelOrder")

        from("jms:queue:startOrderProcessing")
            .routeId("orderProcessingRoute")
            .process { exchange ->
                val orderId = UUID.randomUUID().toString()
                exchange.getIn().setHeader("OrderId", orderId)
            }
            .saga()
            .propagation(SagaPropagation.SUPPORTS)
            .option("OrderId", header("OrderId"))
            .compensation("direct:cancelOrder")
            .process(messageProcessor)
            .log("Start order processing")

            .setBody().simple("\${header.OrderMessage}")
            .log("Send create order message")
            .to("jms:queue:createOrder?disableReplyTo=true")

            .setBody().simple("\${header.PaymentMessage}")
            .log("Send process payment message")
            .to("jms:queue:processPayment?disableReplyTo=true")

            .setBody().simple("\${header.InventoryMessage}")
            .log("Send process inventory message")
            .to("jms:queue:processInventory")

            .to("jms:queue:notifyOrderCompletion?disableReplyTo=true")
            .log("Order completed")


        from("jms:queue:cancelOrderProcessing")
            .to("direct:cancelOrder")

        from("direct:cancelOrder")
            .log("Cancelling order processing: \${header.OrderId}")
            .to("jms:queue:cancelOrder?disableReplyTo=true")
            .to("jms:queue:cancelPayment?disableReplyTo=true")
            .to("jms:queue:revertInventory?disableReplyTo=true")
            .to("jms:queue:notifyOrderCancellation?disableReplyTo=true")

    }
}