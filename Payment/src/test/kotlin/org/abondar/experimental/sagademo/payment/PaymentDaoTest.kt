package org.abondar.experimental.sagademo.payment

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import java.math.BigDecimal
import kotlin.test.assertTrue

@SpringJUnitConfig
@DataJpaTest
class PaymentDaoTest {

    @Autowired
    lateinit var dao: PaymentDao

    @Test
    fun `save payment test`(){
        val payment = Payment(orderId = "test", sum = BigDecimal(23.44), currency = "EUR", paymentType = "Card")

        dao.save(payment)

        assertTrue { payment.id > 0 }
    }

    @Test
    fun `delete payment by order id test`(){
        val payment = Payment(orderId = "test", sum = BigDecimal(23.44), currency = "EUR", paymentType = "Card")
        dao.save(payment)

        dao.deleteByOrderId(payment.orderId)

        val res = dao.findById(payment.id)

        assertTrue { res.isEmpty }
    }


}

