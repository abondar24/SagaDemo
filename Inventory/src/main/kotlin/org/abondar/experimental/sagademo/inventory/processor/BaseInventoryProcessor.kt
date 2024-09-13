package org.abondar.experimental.sagademo.inventory.processor

import com.fasterxml.jackson.databind.ObjectMapper
import org.abondar.experimental.sagademo.inventory.dao.ItemDao
import org.abondar.experimental.sagademo.inventory.dao.OrderInventoryDao
import org.abondar.experimental.sagademo.message.InventoryMessage
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.springframework.transaction.annotation.Transactional

abstract class BaseInventoryProcessor(
    private val objectMapper: ObjectMapper
) : Processor {

    @Transactional
    override fun process(exchange: Exchange?) {

        val request = exchange?.getIn()?.getBody(String::class.java)
        val orderId = exchange?.getIn()?.getHeader("OrderId", String::class.java)

        request?.let {
            val inventoryMessage = objectMapper.readValue(request, InventoryMessage::class.java)
            orderId?.let { orderId ->
                processOrder(orderId, inventoryMessage)
            } ?: throw IllegalArgumentException("Order id missing")
        } ?: throw IllegalArgumentException("Invalid inventory data")

    }

    protected abstract fun processOrder(orderId: String, inventoryMessage: InventoryMessage)


}