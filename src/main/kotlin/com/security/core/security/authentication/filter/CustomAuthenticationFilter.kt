package com.security.core.security.authentication.filter

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
    authenticationManager: AuthenticationManager,
    securityContextRepository: SecurityContextRepository,

): AbstractAuthenticationProcessingFilter(
    requestMatcher, authenticationManager
) {

    init {
        super.setSecurityContextRepository(securityContextRepository)
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val username = request.getParameter("username")
        val password = request.getParameter("password")

        val token = UsernamePasswordAuthenticationToken.unauthenticated(username, password)
        return this.authenticationManager.authenticate(token)
    }
}
