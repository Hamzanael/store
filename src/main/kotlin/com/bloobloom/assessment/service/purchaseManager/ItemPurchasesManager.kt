package com.bloobloom.assessment.service.purchaseManager

import com.bloobloom.assessment.model.ItemPurchase
import com.bloobloom.assessment.model.User

interface ItemPurchasesManager {
    fun purchaseItem(user: User): ItemPurchase
}