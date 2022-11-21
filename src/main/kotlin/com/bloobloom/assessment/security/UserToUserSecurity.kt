package com.bloobloom.assessment.security // ktlint-disable filename

import com.bloobloom.assessment.model.User

fun User.toSecurityDetails(): UserSecurityDetails {
    return UserSecurityDetails(
        this.email,
        this.password,
        this.role.name,
        this.isEnabled,
        this.currency
    )
}
