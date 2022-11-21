package com.bloobloom.assessment.service

import com.bloobloom.assessment.constants.Constants.NUMBER_OF_ALLOWED_QUANTITY
import com.bloobloom.assessment.constants.ConstantsTest.NUMBER_TEN
import com.bloobloom.assessment.constants.ConstantsTest.TEST_FRAME
import com.bloobloom.assessment.constants.ConstantsTest.TEST_LENSES
import com.bloobloom.assessment.constants.ConstantsTest.TEST_SHOPPING_BASKET
import com.bloobloom.assessment.constants.ConstantsTest.TEST_SHOPPING_BASKET_ID
import com.bloobloom.assessment.constants.ConstantsTest.TEST_USER
import com.bloobloom.assessment.constants.ConstantsTest.TEST_USER_ID
import com.bloobloom.assessment.integration.currency.CurrencyConverter
import com.bloobloom.assessment.model.Price
import com.bloobloom.assessment.model.ShoppingBasket
import com.bloobloom.assessment.model.ShoppingBasketItem
import com.bloobloom.assessment.model.repository.ShoppingBasketRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import java.util.Optional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
internal class ShoppingBasketServiceTest {

    @MockK
    private lateinit var shoppingBasketRepository: ShoppingBasketRepository

    @MockK
    private lateinit var frameService: FrameService

    @MockK
    private lateinit var lensesService: LensesService

    @MockK
    private lateinit var currencyConverter: CurrencyConverter

    @InjectMockKs
    private lateinit var shoppingBasketService: ShoppingBasketService

    @BeforeEach
    internal fun setUp() {
        TEST_USER.id = TEST_USER_ID
        TEST_SHOPPING_BASKET.id = TEST_SHOPPING_BASKET_ID
    }

    @Test
    fun `should add to shopping basket even if there is no user exists`() {
        every { shoppingBasketRepository.save(any()) } returns TEST_SHOPPING_BASKET
        every { shoppingBasketRepository.findShoppingBasketByUserId(TEST_USER.id) } returns Optional.ofNullable(null)
        every { currencyConverter.convert(any(), any()) } returns NUMBER_TEN
        val shoppingBasket = shoppingBasketService.getShoppingBasket(TEST_USER)
        verify(atLeast = 1) { shoppingBasketRepository.save(any()) }
        assertThat(shoppingBasket).isEqualTo(TEST_SHOPPING_BASKET)
    }

    @Test
    fun `should get to shopping basket even if there is a user basket`() {
        every { shoppingBasketRepository.findShoppingBasketByUserId(TEST_USER.id) } returns Optional.of(
            TEST_SHOPPING_BASKET
        )
        val shoppingBasket = shoppingBasketService.getShoppingBasket(TEST_USER)
        verify(atMost = 0, atLeast = 0) { shoppingBasketRepository.save(any()) }
        assertThat(shoppingBasket).isEqualTo(TEST_SHOPPING_BASKET)
    }

    @Test
    fun `should checkout all products when the user has items in the basket`() {
        TEST_LENSES.id = "1"
        TEST_FRAME.id = "2"
        val targetShoppingBasket = ShoppingBasket(
            arrayListOf(
                ShoppingBasketItem(TEST_LENSES, NUMBER_OF_ALLOWED_QUANTITY),
                ShoppingBasketItem(TEST_FRAME, NUMBER_OF_ALLOWED_QUANTITY),
            ),
            TEST_USER.id,
            Price(0.0, TEST_USER.currency)
        )
        targetShoppingBasket.id = TEST_SHOPPING_BASKET_ID
        every { shoppingBasketRepository.findShoppingBasketByUserId(TEST_USER.id) } returns Optional.of(
            targetShoppingBasket
        )
        every { shoppingBasketRepository.save(any()) } returns TEST_SHOPPING_BASKET
        every { frameService.buyFrame(any(), any()) } returns TEST_FRAME
        every { lensesService.buyLenses(any(), any()) } returns TEST_LENSES
        every { currencyConverter.convert(any(), any()) } returns NUMBER_TEN
        val shoppingBasket = shoppingBasketService.checkoutShoppingBasket(TEST_USER)
        verify(atLeast = 1) { frameService.buyFrame(any(), any()) }
        verify(atLeast = 1) { lensesService.buyLenses(any(), any()) }

        assertThat(shoppingBasket).isEqualTo(targetShoppingBasket)
    }

}