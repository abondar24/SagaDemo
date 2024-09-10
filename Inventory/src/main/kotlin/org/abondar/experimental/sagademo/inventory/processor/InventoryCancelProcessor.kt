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
        val inventoryMessage = exchange?.getIn()?.getBody(InventoryMessage::class.java)
        if (inventoryMessage != null) {

            inventoryMessage.items.forEach { item ->
                itemDao.updateInventory(item.itemId, null)
                itemDao.updateQuantity(item.itemId, item.quantity)
            }

            orderInventoryDao.deleteByOrderId(inventoryMessage.orderId)

        } else {
            throw IllegalArgumentException("Invalid inventory data")
        }
    }
}
