package com.bloobloom.assessment.service.exceptions.frame

import com.bloobloom.assessment.service.exceptions.ServiceException

class NoFramesInStockAvailable : ServiceException {
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(message: String?) : super(message)
}
