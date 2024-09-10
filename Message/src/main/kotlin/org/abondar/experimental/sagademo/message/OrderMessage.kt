package org.abondar.experimental.sagademo.message

data class OrderMessage(
    val orderId: String,

    val shippingAddress: String,

    val recipientName: String,

    val recipientLastName: String,
)