package com.security.core.security.authentication.provider

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority

class CustomAuthenticationProvider2 : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val loginId = authentication.name
        val password = authentication.credentials as String

        // 아이디, 패스워드 검증 (생략)

        return UsernamePasswordAuthenticationToken.authenticated(loginId, null, listOf(SimpleGrantedAuthority("ROLE_USER")))
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken::class.java)
    }
}
