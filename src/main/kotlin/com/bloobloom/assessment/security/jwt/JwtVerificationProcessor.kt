package com.bloobloom.assessment.security.jwt

import com.bloobloom.assessment.model.Currency
import com.bloobloom.assessment.model.enums.Role
import com.bloobloom.assessment.security.AuthorizedUser
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
class JwtVerificationProcessor {

    @Autowired
    private lateinit var jwtConfiguration: JwtConfiguration

    fun isValidAuthorizationHeader(header: String?): Boolean {
        return !header.isNullOrBlank() && header.startsWith(jwtConfiguration.tokenPrefix)
    }

    fun extractTokenFromHeader(header: String): String {
        return header.replace("Bearer ", "")
    }

    fun parsingToken(token: String): Jws<Claims> {
        return Jwts.parserBuilder().setSigningKey(jwtConfiguration.getSecretKey()).build().parseClaimsJws(token)
    }

    @Suppress("UNCHECKED_CAST")
    fun authenticateUser(parsedToken: Jws<Claims>, request: HttpServletRequest) {
        val jwtBody = parsedToken.body
        val username = jwtBody.subject
        val authorities = jwtBody["authorities"] as List<Map<String, String>>
        val role = Role.valueOf(authorities.first()["role"].toString().substring(5))
        val currency = Currency.valueOf(jwtBody["currency"].toString())
        val authorizationList = authorities.map {
            SimpleGrantedAuthority(it["role"])
        }.toList()
        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            AuthorizedUser(
                username,
                role,
                currency
            ),
            null,
            authorizationList
        )
    }
}
