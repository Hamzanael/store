package com.bloobloom.assessment.integration.currency

import com.bloobloom.assessment.model.Currency

abstract class CurrencyAPIResponse(
    open val success: Boolean,
    open val timestamp: Int,
    open val base: String,
    open val date: String,
    open val rates: Map<Currency, Double>
)