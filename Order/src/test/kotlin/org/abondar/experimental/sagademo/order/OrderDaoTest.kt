package org.abondar.experimental.sagademo.order

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import kotlin.test.assertTrue

@DataJpaTest
class OrderDaoTest {

    @Autowired
    lateinit var orderDao: OrderDao

    @Test
    fun `order dao delete test`(){
        val order = Order(orderId = "test", recipientName = "test", shippingAddress = "test", recipientLastName = "test")

        orderDao.save(order)

        orderDao.deleteByOrderId(order.orderId)

        val res = orderDao.findById(order.id)

       assertTrue { res.isEmpty }

    }


}