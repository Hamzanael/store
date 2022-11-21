package com.bloobloom.assessment.service.exceptions

class NoEntityFoundException : ServiceException {
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(message: String?) : super(message)
}
