package com.bloobloom.assessment.model

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class ItemPurchase(
    val shoppingBasket: ShoppingBasket,
    val price: Price,
    val soldTo: String, // user id
    val soldAt: LocalDateTime = LocalDateTime.now(),
) {
    @Id
    @Schema(hidden = true)
    lateinit var id: String
}

