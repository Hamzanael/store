package com.bloobloom.assessment.api.v1.request

import com.bloobloom.assessment.model.Price
import com.bloobloom.assessment.model.Status

data class UpdateFrameRequest(
    val name: String? = null,
    val description: String? = null,
    val stock: Int? = null,
    val price: Price? = null,
    val status: Status? = null
)
