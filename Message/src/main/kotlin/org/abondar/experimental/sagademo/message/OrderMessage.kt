package org.abondar.experimental.sagademo.message

data class OrderMessage(
    val shippingAddress: String,

    val recipientName: String,

    val recipientLastName: String,
)