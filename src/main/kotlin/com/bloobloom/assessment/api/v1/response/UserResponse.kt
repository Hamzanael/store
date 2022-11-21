package com.bloobloom.assessment.api.v1.response

import com.bloobloom.assessment.model.enums.Role

data class UserResponse(
    val id: String,
    val fullName: String,
    val email: String,
    val role: Role,
    val enabled: Boolean,
)
