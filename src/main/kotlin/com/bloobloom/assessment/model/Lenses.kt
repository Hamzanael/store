package com.bloobloom.assessment.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Lenses(
    override val name: String,
    val color: String,
    val description: String,
    @JsonProperty("prescription_type") val prescriptionType: PrescriptionType,
    @JsonProperty("lenses_type") val lensesType: LensesType,
    override val stock: Int,
    override var price: Price,
) : Product {
    @Schema(hidden = true)
    @Id
    override lateinit var id: String

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var prices = setOf<Price>()
}

enum class PrescriptionType {
    FASHION, SINGLE_VISION, VARIFOCAL
}

enum class LensesType {
    CLASSIC, BLUE_LIGHT, TRANSITION
}