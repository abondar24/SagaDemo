package org.abondar.experimental.sagademo.inventory.dao

import org.abondar.experimental.sagademo.inventory.data.Item
import org.abondar.experimental.sagademo.inventory.data.OrderInventory
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@DataJpaTest
class ItemDaoTest {

    @Autowired
    lateinit var dao: ItemDao

    @Autowired
    lateinit var orderInventoryDao: OrderInventoryDao

    @Test
    fun `save item test`() {
        val item = Item(itemId = UUID.randomUUID().toString(), name = "test", quantity = 1)

        dao.save(item)

        assertTrue { item.id > 0 }

    }

    @Test
    fun `find item by item id test`(){
        val item = Item(itemId = UUID.randomUUID().toString(), name = "test", quantity = 1)
        dao.save(item)

        val result = dao.findByItemIdIn(listOf(item.itemId))

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertEquals(result.size, 1)
    }


    @Test
    fun `update item quantity test`() {
        val item = Item(itemId = UUID.randomUUID().toString(), name = "test", quantity = 1)
        dao.save(item)

        dao.updateQuantity(item.itemId, 7)

        val res = dao.findById(item.id)

        assertTrue { res.isPresent }
        assertEquals(7, res.get().quantity)

    }

    @Test
    fun `update item order inventory test`() {
        val item = Item(itemId = UUID.randomUUID().toString(), name = "test", quantity = 1)
        dao.save(item)

        val orderInventory = OrderInventory(orderId = "test1243", items = listOf(item))
        orderInventoryDao.save(orderInventory)

        dao.updateInventory(item.itemId, orderInventory)

        val res = dao.findById(item.id)

        assertTrue { res.isPresent }
        assertNotNull(res.get().orderInventory)
        assertEquals(orderInventory.orderId, res.get().orderInventory!!.orderId)

    }



}