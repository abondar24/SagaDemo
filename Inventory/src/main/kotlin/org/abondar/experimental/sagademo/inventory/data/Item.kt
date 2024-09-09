package org.abondar.experimental.sagademo.inventory.data

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "item")
data class Item(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @NotNull
    @Column(name = "item_id")
    val itemId: String,

    @NotNull
    val name: String,

    @NotNull
    val quantity: Int,

    @ManyToOne
    @JoinColumn(name = "order_inventory_id")
    val orderInventory: OrderInventory? = null
)
