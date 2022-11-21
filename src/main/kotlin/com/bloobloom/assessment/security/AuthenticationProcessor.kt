package com.bloobloom.assessment.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

fun authorizedUser() = authenticatedUser().principal as AuthorizedUser

private fun authenticatedUser(): Authentication = SecurityContextHolder.getContext().authentication
