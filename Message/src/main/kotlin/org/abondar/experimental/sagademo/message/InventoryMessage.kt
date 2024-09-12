package org.abondar.experimental.sagademo.message

data class InventoryMessage(
    val items: List<ItemMessage>
)
