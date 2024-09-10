package org.abondar.experimental.sagademo.order

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface OrderDao : JpaRepository<Order,Long> {

    @Modifying(clearAutomatically = true)
    fun deleteByOrderId(@Param("orderId") orderId: String)
}