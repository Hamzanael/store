package com.bloobloom.assessment.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class ShoppingBasket(
    val items: ArrayList<ShoppingBasketItem>,
    val userId: String,
    var total: Price
) {
    @Id
    lateinit var id: String
}

data class ShoppingBasketItem(
    var product: Product,
    val quantity: Int
)