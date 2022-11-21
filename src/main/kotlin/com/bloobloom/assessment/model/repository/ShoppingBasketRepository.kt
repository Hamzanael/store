package com.bloobloom.assessment.model.repository

import com.bloobloom.assessment.model.ShoppingBasket
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ShoppingBasketRepository : MongoRepository<ShoppingBasket, String> {
    fun findShoppingBasketByUserId(userId: String): Optional<ShoppingBasket>
}

