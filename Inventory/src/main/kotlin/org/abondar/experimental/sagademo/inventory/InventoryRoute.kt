package org.abondar.experimental.sagademo.inventory

import org.abondar.experimental.sagademo.inventory.processor.InventoryCancelProcessor
import org.abondar.experimental.sagademo.inventory.processor.InventoryProcessor
import org.apache.camel.builder.RouteBuilder
import org.springframework.stereotype.Component


@Component
class InventoryRoute(
    private val processor: InventoryProcessor,
    private val cancelProcessor: InventoryCancelProcessor,
) : RouteBuilder() {
    override fun configure() {

        onException(Exception::class.java)
            .handled(true)
            .log("Exception occured : \${exception.message}")

        from("jms:queue:processInventory")
            .routeId("inventoryRoute")
            .process(processor)
            .log("Inventory processed for order \${header.orderId}")
            .end()

        from("jms:queue:revertInventory")
            .routeId("revertInventoryRoute")
            .log("Revert inventory for order \${header.orderId}")
            .process(cancelProcessor)
            .end()

    }
}