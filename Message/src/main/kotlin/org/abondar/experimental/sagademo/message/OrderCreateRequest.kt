package org.abondar.experimental.sagademo.message

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class OrderCreateRequest(

    @JsonProperty("recipient_name")
    val recipientName: String,

    @JsonProperty("recipient_last_name")
    val recipientLastName: String,

    @JsonProperty("shipping_address")
    val shippingAddress: String,

    val sum: BigDecimal,

    val currency: String,

    @JsonProperty("payment_type")
    val paymentType: String,

    val items: List<ItemMessage>
)
