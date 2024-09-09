package org.abondar.experimental.sagademo.inventory.dao

import org.abondar.experimental.sagademo.inventory.data.Item
import org.abondar.experimental.sagademo.inventory.data.OrderInventory
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.util.*
import kotlin.test.assertTrue


@DataJpaTest
class OrderInventoryDaoTest {

    @Autowired
    lateinit var dao: OrderInventoryDao

    @Autowired
    lateinit var itemDao: ItemDao

    @Test
    fun `save  order inventory test`(){
        val item = Item(itemId = UUID.randomUUID().toString(), name = "test", quantity = 1)
        itemDao.save(item)

        val orderInventory = OrderInventory(orderId = "test",items = listOf(item))
        dao.save(orderInventory)

        assertTrue { orderInventory.id > 0 }
    }

    @Test
    fun `delete order inventory by orderId`(){
        val item = Item(itemId = UUID.randomUUID().toString(), name = "test", quantity = 1)
        itemDao.save(item)

        val orderInventory = OrderInventory(orderId = "test",items = listOf(item))
        dao.save(orderInventory)

        dao.deleteByOrderId(orderInventory.orderId)

        val res = dao.findById(orderInventory.id)
        assertTrue(res.isEmpty)

        val itemRes = itemDao.findById(item.id)
        assertTrue(itemRes.isPresent)

    }


}