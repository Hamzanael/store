package com.bloobloom.assessment.api.v1.request

import com.bloobloom.assessment.model.enums.Role

data class UserRequest(
        val fullName: String,
        val email: String,
        val role: Role,
        val accountId: String
)
