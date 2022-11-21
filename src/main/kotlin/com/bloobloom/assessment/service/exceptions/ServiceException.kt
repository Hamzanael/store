package com.bloobloom.assessment.service.exceptions

open class ServiceException : Exception {
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(message: String?) : super(message)
}
