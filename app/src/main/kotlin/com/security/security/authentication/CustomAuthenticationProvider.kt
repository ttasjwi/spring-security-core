package com.security.security.authentication

import com.security.security.event.CustomAuthenticationFailureEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User

class CustomAuthenticationProvider(
    private val applicationEventPublisher: ApplicationEventPublisher,
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        if (authentication.name != "user") {
            val exception = BadCredentialsException("아이디가 user가 아님...")
            applicationEventPublisher.publishEvent(CustomAuthenticationFailureEvent(authentication, exception))
            throw exception
        }

        val user = User.withUsername("user").password("{noop}1111").roles("USER").build()
        return UsernamePasswordAuthenticationToken.authenticated(user, user.password, user.authorities)
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return true
    }
}
