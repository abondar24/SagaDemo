package org.abondar.experimental.sagademo.inventory.dao

import org.abondar.experimental.sagademo.inventory.data.OrderInventory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface OrderInventoryDao : JpaRepository<OrderInventory, Long> {

    @Modifying(clearAutomatically = true)
    fun deleteByOrderId(@Param("orderId") orderId: String)

}