package com.bloobloom.assessment.service.exceptions.lenses

import com.bloobloom.assessment.service.exceptions.ServiceException

class NoLensesInStockAvailable : ServiceException {
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(message: String?) : super(message)
}
