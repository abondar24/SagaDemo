package org.abondar.experimental.sagademo.inventory.data

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "order_inventory")
data class OrderInventory(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @NotNull
    @Column(name = "order_id")
    val orderId: String,

    @OneToMany
    @JoinColumn(name = "order_inventory_id")
    val items: List<Item>

    )
