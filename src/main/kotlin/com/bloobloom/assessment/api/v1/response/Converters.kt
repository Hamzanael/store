package com.bloobloom.assessment.api.v1.response

import com.bloobloom.assessment.api.v1.request.UserRequest
import com.bloobloom.assessment.model.User

fun UserRequest.toUser(): User {
    return User(
        this.fullName,
        this.email,
        "",
        this.role,
    )
}


fun User.toResponse(): UserResponse {
    return UserResponse(
        this.id,
        this.fullName,
        this.email,
        this.role,
        this.isEnabled,
    )
}
