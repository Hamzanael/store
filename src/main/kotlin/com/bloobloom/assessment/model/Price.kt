package com.bloobloom.assessment.model

data class Price(
    val value: Double,
    val currency: Currency
)

enum class Currency {
    USD, GBP, EUR, JOD, JPY
}
