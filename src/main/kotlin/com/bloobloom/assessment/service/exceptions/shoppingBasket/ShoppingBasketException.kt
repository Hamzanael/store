package com.bloobloom.assessment.service.exceptions.shoppingBasket

import com.bloobloom.assessment.service.exceptions.ServiceException

class ShoppingBasketException : ServiceException {
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(message: String?) : super(message)
}
