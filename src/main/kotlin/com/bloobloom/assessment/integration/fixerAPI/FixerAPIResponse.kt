package com.bloobloom.assessment.integration.fixerAPI

import com.bloobloom.assessment.integration.currency.CurrencyAPIResponse
import com.bloobloom.assessment.model.Currency

data class FixerAPIResponse(
    override val success: Boolean,
    override val timestamp: Int,
    override val base: String,
    override val date: String,
    override val rates: Map<Currency, Double>
) : CurrencyAPIResponse(success, timestamp, base, date, rates)