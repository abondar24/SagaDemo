package org.abondar.experimental.sagademo.inventory.processor

import com.fasterxml.jackson.databind.ObjectMapper
import org.abondar.experimental.sagademo.inventory.dao.ItemDao
import org.abondar.experimental.sagademo.inventory.dao.OrderInventoryDao
import org.abondar.experimental.sagademo.inventory.data.OrderInventory
import org.abondar.experimental.sagademo.message.InventoryMessage
import org.springframework.stereotype.Component

@Component
class InventoryProcessor(
    private val itemDao: ItemDao,
    private val orderInventoryDao: OrderInventoryDao,
    objectMapper: ObjectMapper
) : BaseInventoryProcessor(objectMapper) {

    override fun processOrder(orderId: String, inventoryMessage: InventoryMessage) {
        val itemIds: List<String> = inventoryMessage.items.map { it.itemId }
        val items = itemDao.findByItemIdIn(itemIds)

        val orderInventory = OrderInventory(orderId = orderId, items = items)
        orderInventoryDao.save(orderInventory)

        inventoryMessage.items.forEach { item ->
            itemDao.updateInventory(item.itemId, orderInventory)
            itemDao.updateQuantity(item.itemId, item.quantity)
        }

        orderInventoryDao.deleteByOrderId(orderId)
    }

}