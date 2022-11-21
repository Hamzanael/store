package com.bloobloom.assessment.integration.fixerAPI

class FixerAPIException : RuntimeException {
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}