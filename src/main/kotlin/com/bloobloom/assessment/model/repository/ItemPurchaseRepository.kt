package com.bloobloom.assessment.model.repository

import com.bloobloom.assessment.model.ItemPurchase
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemPurchaseRepository : MongoRepository<ItemPurchase, String>

