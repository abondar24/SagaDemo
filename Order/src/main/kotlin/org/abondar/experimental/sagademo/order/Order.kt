package org.abondar.experimental.sagademo.order

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "ms_order")
data class Order(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @NotNull
    @Column(name = "order_id")
    val orderId: String,

    @NotNull
    @Column(name = "shipping_address")
    val shippingAddress: String,

    @NotNull
    @Column(name = "recipient_name")
    val recipientName: String,

    @NotNull
    @Column(name = "recipient_last_name")
    val recipientLastName: String,
)
