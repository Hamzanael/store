package com.bloobloom.assessment.service

import com.bloobloom.assessment.model.ItemPurchase
import com.bloobloom.assessment.model.User
import com.bloobloom.assessment.model.repository.ItemPurchaseRepository
import com.bloobloom.assessment.service.purchaseManager.ItemPurchasesManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ItemPurchaseService : ItemPurchasesManager {
    @Autowired
    lateinit var itemPurchaseRepository: ItemPurchaseRepository

    @Autowired
    lateinit var shoppingBasketService: ShoppingBasketService

    override fun purchaseItem(user: User): ItemPurchase {
        logger.debug("user with id ${user.id} is trying to checkout his shopping basket")
        val shoppingBasket = shoppingBasketService.checkoutShoppingBasket(user)
        logger.debug("user with id ${user.id} checked out his shopping basket")
        val itemPurchase = ItemPurchase(
            shoppingBasket = shoppingBasket, price = shoppingBasket.total, soldTo = user.id
        )
        logger.debug("user with id ${user.id} is trying to save his purchase")
        return itemPurchaseRepository.save(itemPurchase)
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ItemPurchaseService::class.java)
    }
}
