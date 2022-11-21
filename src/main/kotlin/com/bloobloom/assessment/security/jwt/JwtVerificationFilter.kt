package com.bloobloom.assessment.security.jwt

import io.jsonwebtoken.JwtException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtVerificationFilter(
    private var verificationProcessor: JwtVerificationProcessor
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filter: FilterChain) {
        val authorizationHeader = request.getHeader("Authorization")
        if (verificationProcessor.isValidAuthorizationHeader(authorizationHeader)) {
            verificationProcessor.authenticateUser(getToken(authorizationHeader), request)
        } else {
            log.debug("unauthorized request from IP ${request.getHeader("X-FORWARDED-FOR")}")
        }
        filter.doFilter(request, response)
    }

    private fun getToken(authorizationHeader: String) = try {
        verificationProcessor.parsingToken(
            verificationProcessor.extractTokenFromHeader(authorizationHeader)
        )
    } catch (jwtException: JwtException) {
        log.error("Illegal exception: token cannot be trusted")
        throw IllegalStateException("Token cannot be trusted")
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(JwtVerificationFilter::class.java)
    }
}
