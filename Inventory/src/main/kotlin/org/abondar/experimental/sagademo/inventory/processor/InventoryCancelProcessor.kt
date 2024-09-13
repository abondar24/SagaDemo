package org.abondar.experimental.sagademo.inventory.processor

import com.fasterxml.jackson.databind.ObjectMapper
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
    private val orderInventoryDao: OrderInventoryDao,
    objectMapper: ObjectMapper
) : BaseInventoryProcessor(objectMapper) {

    override fun processOrder(orderId: String, inventoryMessage: InventoryMessage) {
        inventoryMessage.items.forEach { item ->
            itemDao.updateInventory(item.itemId, null)
            itemDao.updateQuantity(item.itemId, item.quantity)
        }

        orderInventoryDao.deleteByOrderId(orderId)
    }
}
