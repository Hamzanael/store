package com.bloobloom.assessment.service.exceptions.security

import com.bloobloom.assessment.service.exceptions.ServiceException

class WeakPasswordException : ServiceException {
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}
