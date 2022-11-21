package com.bloobloom.assessment.constants

import com.bloobloom.assessment.api.v1.request.UpdateFrameRequest
import com.bloobloom.assessment.api.v1.request.UpdateLensesRequest
import com.bloobloom.assessment.constants.Constants.DEFAULT_CURRENCY
import com.bloobloom.assessment.model.Currency
import com.bloobloom.assessment.model.Frame
import com.bloobloom.assessment.model.ItemPurchase
import com.bloobloom.assessment.model.Lenses
import com.bloobloom.assessment.model.LensesType
import com.bloobloom.assessment.model.PrescriptionType
import com.bloobloom.assessment.model.Price
import com.bloobloom.assessment.model.ShoppingBasket
import com.bloobloom.assessment.model.Status
import com.bloobloom.assessment.model.User
import com.bloobloom.assessment.model.enums.Role

object ConstantsTest {
    val NUMBER_TEN = 10.0
    val TEST_FRAME = Frame(
        "Test frame", "Testing frame", 4, Price(NUMBER_TEN, Currency.USD), Status.ACTIVE
    )
    val TEST_UPDATE_FRAME_REQUEST = UpdateFrameRequest(
        name = "Updated Name",
        description = "Updated Description",
        price = Price(NUMBER_TEN, Currency.USD),
        stock = 10,
        status = Status.ACTIVE
    )


    val TEST_LENSES = Lenses(
        name = "test",
        description = "test",
        prescriptionType = PrescriptionType.FASHION,
        lensesType = LensesType.BLUE_LIGHT,
        color = "white",
        stock = 4,
        price = Price(
            currency = Currency.USD, value = 10.0
        )
    )
    val TEST_UPDATE_LENSES_REQUEST = UpdateLensesRequest(
        description = "Updated Description", price = Price(NUMBER_TEN, Currency.USD), stock = 10
    )
    const val TEST_USER_ID = "1"
    const val TEST_SHOPPING_BASKET_ID = "1"
    val TEST_USER = User(
        fullName = "Test", email = "test@example.com", "anyOtherData", Role.USER, isEnabled = true, Currency.JOD
    )
    val TEST_SHOPPING_BASKET = ShoppingBasket(
        arrayListOf(), TEST_USER_ID, Price(0.0, Currency.JOD)
    )
    val EMPTY_PRICE = Price(0.0, DEFAULT_CURRENCY)
    val TEST_ITEM_PURCHASE = ItemPurchase(
        TEST_SHOPPING_BASKET, EMPTY_PRICE, soldTo = TEST_USER_ID
    )
}