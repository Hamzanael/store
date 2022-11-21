package com.bloobloom.assessment.api.v1.request

import com.fasterxml.jackson.annotation.JsonProperty

data class ShoppingBasketRequest(
    @JsonProperty("lenses_id_and_quantity") val lensesIdAndQuantity: Pair<String, Int>,
    @JsonProperty("frame_id_and_quantity") val frameIdAndQuantity: Pair<String, Int>
)