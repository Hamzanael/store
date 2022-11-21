package com.bloobloom.assessment.service.exceptions.security

class FailureAuthenticationException : Exception {
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}
