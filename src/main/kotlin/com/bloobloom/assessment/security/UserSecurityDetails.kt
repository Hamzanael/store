package com.bloobloom.assessment.security

import com.bloobloom.assessment.model.Currency
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class UserSecurityDetails(
    private val userName: String,
    private val password: String,
    private val role: String,
    private val enabled: Boolean,
    private val currency: Currency
) : UserDetails {

    override fun getAuthorities(): List<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_$role"))
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return userName
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    fun getCurrency(): Currency {
        return currency
    }

}
