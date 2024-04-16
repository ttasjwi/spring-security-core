package com.security.core.filter

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.context.SecurityContextRepository
import org.springframework.security.web.util.matcher.RequestMatcher

class CustomAuthenticationFilter(
    requestMatcher: RequestMatcher,
    securityContextRepository: SecurityContextRepository,
    authenticationManager: AuthenticationManager
) : AbstractAuthenticationProcessingFilter(requestMatcher, authenticationManager) {

    init {
        setSecurityContextRepository(securityContextRepository)
    }

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {

        val username = request!!.getParameter("username")
        val password = request.getParameter("password")

        val token = UsernamePasswordAuthenticationToken(username, password)

        return authenticationManager.authenticate(token)
    }

}
