package com.bloobloom.assessment.security.jwt

import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import javax.crypto.SecretKey

@ConfigurationProperties(prefix = "application.jwt")
@Configuration
class JwtConfiguration {
    lateinit var secretKey: String
    lateinit var tokenExpirationAfterDays: String
    val tokenPrefix: String = "Bearer "

    fun getSecretKey(): SecretKey {
        return Keys.hmacShaKeyFor(secretKey.toByteArray())
    }
}
