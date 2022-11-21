package com.bloobloom.assessment.api.v1.request

import com.bloobloom.assessment.model.LensesType
import com.bloobloom.assessment.model.PrescriptionType
import com.bloobloom.assessment.model.Price
import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateLensesRequest(
    val color: String? = null,
    val description: String? = null,
    @JsonProperty("prescription_type")
    val prescriptionType: PrescriptionType? = null,
    @JsonProperty("lenses_type")
    val lensesType: LensesType? = null,
    val stock: Int? = null,
    val price: Price? = null,
)
