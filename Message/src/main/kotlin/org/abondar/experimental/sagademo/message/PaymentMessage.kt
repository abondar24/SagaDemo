package org.abondar.experimental.sagademo.message

import java.math.BigDecimal

data class PaymentMessage(
    val orderId: String,

    val sum: BigDecimal,

    val currency: String,

    val paymentType: String
)
