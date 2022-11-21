package com.bloobloom.assessment.service.exceptions

class DuplicatedEntityException : ServiceException {
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(message: String?) : super(message)
}
