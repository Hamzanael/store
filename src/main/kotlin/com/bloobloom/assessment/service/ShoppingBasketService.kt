package com.bloobloom.assessment.service

import com.bloobloom.assessment.constants.Constants.DEFAULT_CURRENCY
import com.bloobloom.assessment.constants.Constants.NUMBER_OF_ALLOWED_QUANTITY
import com.bloobloom.assessment.integration.currency.CurrencyConverter
import com.bloobloom.assessment.model.*
import com.bloobloom.assessment.model.repository.ShoppingBasketRepository
import com.bloobloom.assessment.service.exceptions.shoppingBasket.ShoppingBasketException
import com.bloobloom.assessment.util.calculatePrice
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ShoppingBasketService {

    @Autowired
    private lateinit var shoppingBasketRepository: ShoppingBasketRepository

    @Autowired
    private lateinit var lensesService: LensesService

    @Autowired
    private lateinit var frameService: FrameService

    @Autowired
    private lateinit var currencyConverter: CurrencyConverter

    fun addToShoppingBasket(
        user: User, lensesIdAndQuantity: Pair<String, Int>, frameIdAndQuantity: Pair<String, Int>
    ): ShoppingBasket {
        // verify quantity for assessment requirement
        val isOneQuantity =
            (lensesIdAndQuantity.second != NUMBER_OF_ALLOWED_QUANTITY || frameIdAndQuantity.second != NUMBER_OF_ALLOWED_QUANTITY)
        if (isOneQuantity) {
            logger.error("user with id ${user.id} is trying to add more than one quantity of lenses or frame")
            throw ShoppingBasketException("Quantity must be 1")
        }
        kotlin.runCatching {
            val lenses = lensesService.getLensesIfAvailable(lensesIdAndQuantity.first)
            val frame = frameService.getFrameIfAvailable(frameIdAndQuantity.first)
            val shoppingBasket = shoppingBasketRepository.findShoppingBasketByUserId(user.id).orElseGet {
                ShoppingBasket(
                    userId = user.id, items = arrayListOf(), total = Price(
                        currency = DEFAULT_CURRENCY,
                        value = 0.0
                    )
                )
            }
            shoppingBasket.items.add(
                ShoppingBasketItem(
                    product = lenses, quantity = lensesIdAndQuantity.second
                )
            )
            shoppingBasket.items.add(
                ShoppingBasketItem(
                    product = frame, quantity = frameIdAndQuantity.second
                )
            )
            shoppingBasket.total = calculatePrice(shoppingBasket.items.map { it.product.price }.toList())
            return convertProductsCurrency(shoppingBasketRepository.save(shoppingBasket), user.currency)
        }.getOrElse { throw ShoppingBasketException("Could not add to shopping basket due to $it") }
    }

    fun getShoppingBasket(user: User): ShoppingBasket {
        val shoppingBasket = shoppingBasketRepository.findShoppingBasketByUserId(user.id).orElseGet {
            shoppingBasketRepository.save(
                ShoppingBasket(
                    userId = user.id, items = arrayListOf(), total = Price(
                        currency = DEFAULT_CURRENCY,
                        value = 0.0
                    )
                )
            )
        }
        return convertProductsCurrency(shoppingBasket, user.currency)
    }


    fun checkoutShoppingBasket(user: User): ShoppingBasket {
        val emptyBasket = ShoppingBasket(
            userId = user.id, items = arrayListOf(), total = Price(
                currency = DEFAULT_CURRENCY,
                value = 0.0
            )
        )
        val shoppingBasket = shoppingBasketRepository.findShoppingBasketByUserId(user.id).orElseGet {
            shoppingBasketRepository.save(
                emptyBasket
            )
        }
        buyingProducts(shoppingBasket)
        emptyBasket.id = shoppingBasket.id
        shoppingBasketRepository.save(emptyBasket)
        return shoppingBasket
    }

    private fun buyingProducts(shoppingBasket: ShoppingBasket) {
        shoppingBasket.items.forEach {
            when (it.product) {
                is Lenses -> lensesService.buyLenses(it.product.id, it.quantity)
                is Frame -> frameService.buyFrame(it.product.id, it.quantity)
            }
        }
    }

    private fun convertProductsCurrency(
        shoppingBasket: ShoppingBasket, currency: Currency
    ): ShoppingBasket {
        val convertedShoppingBasket = shoppingBasket.copy(
            items = shoppingBasket.items.map { shoppingBasketItem ->
                val product = shoppingBasketItem.product
                product.price = Price(currencyConverter.convert(product.price.value, currency), currency)
                ShoppingBasketItem(
                    product, shoppingBasketItem.quantity
                )
            }.toCollection(arrayListOf())
        )
        convertedShoppingBasket.id = shoppingBasket.id
        return convertedShoppingBasket
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ShoppingBasketService::class.java)
    }
}
