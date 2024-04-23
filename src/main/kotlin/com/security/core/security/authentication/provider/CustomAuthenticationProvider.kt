package com.security.core.security.authentication.provider

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService

class CustomAuthenticationProvider(
    private val userDetailsService: UserDetailsService
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val loginId = authentication.name
        val password = authentication.credentials as String

        // 아이디에 대응되는 회원 조회
        val user = userDetailsService.loadUserByUsername(loginId)

        // 패스워드 검증 (생략)

        return UsernamePasswordAuthenticationToken.authenticated(user.username, null, user.authorities)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken::class.java)
    }
}
