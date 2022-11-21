package com.bloobloom.assessment.model

interface Product {
    val id: String
    val name: String
    var price: Price
    val stock: Int
}
