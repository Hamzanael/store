package com.bloobloom.assessment.model

import com.bloobloom.assessment.constants.Constants.DEFAULT_CURRENCY
import com.bloobloom.assessment.model.enums.Role
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class User(
    val fullName: String,
    @Indexed(unique = true) val email: String,
    val password: String,
    val role: Role,
    val isEnabled: Boolean = true,
    val currency: Currency = DEFAULT_CURRENCY,
) {
    @Schema(hidden = true)
    @Id
    lateinit var id: String
}
