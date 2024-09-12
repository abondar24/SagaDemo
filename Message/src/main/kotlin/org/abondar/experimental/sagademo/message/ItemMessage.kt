package org.abondar.experimental.sagademo.message

import com.fasterxml.jackson.annotation.JsonProperty

data class ItemMessage(

    @JsonProperty("item_id")
    val itemId: String,

    val quantity: Int,
)
