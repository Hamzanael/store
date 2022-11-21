package com.bloobloom.assessment.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Frame(
    override val name: String,
    val description: String,
    override val stock: Int,
    override var price: Price,
    val status: Status
) : Product {

    @Schema(hidden = true)
    @Id
    override lateinit var id: String

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var prices = setOf<Price>()
}
