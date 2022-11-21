package com.bloobloom.assessment.service

import com.bloobloom.assessment.constants.ConstantsTest.TEST_ITEM_PURCHASE
import com.bloobloom.assessment.constants.ConstantsTest.TEST_SHOPPING_BASKET
import com.bloobloom.assessment.constants.ConstantsTest.TEST_USER
import com.bloobloom.assessment.constants.ConstantsTest.TEST_USER_ID
import com.bloobloom.assessment.model.repository.ItemPurchaseRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
internal class ItemPurchaseServiceTest {

    @MockK
    private lateinit var itemPurchaseRepository: ItemPurchaseRepository

    @MockK
    private lateinit var shoppingBasketService: ShoppingBasketService

    @InjectMockKs
    private lateinit var itemPurchaseService: ItemPurchaseService


    @Test
    fun purchaseItem() {
        TEST_SHOPPING_BASKET.id = "1"
        TEST_USER.id = TEST_USER_ID
        every { shoppingBasketService.checkoutShoppingBasket(any()) } returns TEST_SHOPPING_BASKET
        every { itemPurchaseRepository.save(any()) } returns TEST_ITEM_PURCHASE
        val itemPurchase = itemPurchaseService.purchaseItem(TEST_USER)
        assertNotNull(itemPurchase)
    }
}