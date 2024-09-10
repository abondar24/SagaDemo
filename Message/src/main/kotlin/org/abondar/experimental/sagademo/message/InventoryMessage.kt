package org.abondar.experimental.sagademo.message

data class InventoryMessage(
    val orderId: String,

    val items: List<ItemMessage>
)
