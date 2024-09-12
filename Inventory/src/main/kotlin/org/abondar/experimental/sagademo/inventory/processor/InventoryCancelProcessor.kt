package org.abondar.experimental.sagademo.inventory.processor

import org.abondar.experimental.sagademo.inventory.dao.ItemDao
import org.abondar.experimental.sagademo.inventory.dao.OrderInventoryDao
import org.abondar.experimental.sagademo.message.InventoryMessage
import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional


@Component
class InventoryCancelProcessor(
    private val itemDao: ItemDao,
    private val orderInventoryDao: OrderInventoryDao
) : Processor {

    @Transactional
    override fun process(exchange: Exchange?) {

        val orderId = exchange?.getIn()?.getHeader("OrderId", String::class.java)

        exchange?.getIn()?.getHeader("InventoryMessage", InventoryMessage::class.java)?.let { inventoryMessage ->

            orderId?.let { orderId ->
                inventoryMessage.items.forEach { item ->
                    itemDao.updateInventory(item.itemId, null)
                    itemDao.updateQuantity(item.itemId, item.quantity)
                }

                orderInventoryDao.deleteByOrderId(orderId)
            } ?: throw IllegalArgumentException("Order id missing")


        } ?: throw IllegalArgumentException("Invalid inventory data")


    }
}
