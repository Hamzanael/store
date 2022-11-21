package com.bloobloom.assessment.security

import com.bloobloom.assessment.model.Currency
import com.bloobloom.assessment.model.enums.Role

class AuthorizedUser(
    val username: String,
    val role: Role,
    val currency: Currency
)
