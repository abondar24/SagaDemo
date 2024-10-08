package org.abondar.experimental.sagademo.inventory.dao

import org.abondar.experimental.sagademo.inventory.data.Item
import org.abondar.experimental.sagademo.inventory.data.OrderInventory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ItemDao : JpaRepository<Item, Long> {


    @Query("SELECT i FROM Item i WHERE i.itemId IN :itemIds")
    fun findByItemIdIn(@Param("itemIds") itemIds: List<String>): List<Item>

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Item i SET i.quantity = :quantity WHERE i.itemId = :itemId")
    fun updateQuantity(@Param("itemId") itemId: String, @Param("quantity") quantity: Int)

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Item i SET i.orderInventory = :orderInventory WHERE i.itemId = :itemId")
    fun updateInventory(@Param("itemId") itemId: String, @Param("orderInventory") orderInventory: OrderInventory?)
}