package com.bloobloom.assessment.integration.currency

import com.bloobloom.assessment.model.Currency

interface CurrencyConverter {
    fun getCurrencyRates(): CurrencyAPIResponse
    fun convert(value: Double, currency: Currency): Double
}