package com.bloobloom.assessment.util

import com.bloobloom.assessment.constants.Constants.DEFAULT_CURRENCY
import com.bloobloom.assessment.model.Currency.USD
import com.bloobloom.assessment.model.Price


fun calculatePrice(prices: List<Price>): Price {
    if (prices.isEmpty()) {
        return Price(0.0, DEFAULT_CURRENCY)
    }
    return Price(prices.sumOf { it.value }, prices.first().currency)
}

