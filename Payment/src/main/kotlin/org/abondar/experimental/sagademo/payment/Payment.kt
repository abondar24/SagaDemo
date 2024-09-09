package org.abondar.experimental.sagademo.payment

import jakarta.persistence.*
import org.jetbrains.annotations.NotNull
import java.math.BigDecimal

@Entity
@Table(name = "payment")
data class Payment (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @NotNull
    @Column(name= "order_id")
    val orderId: String,

    @NotNull
    val sum: BigDecimal,

    @NotNull
    val currency: String,

    @Column(name="payment_type")
    @NotNull
    val paymentType: String
    )
