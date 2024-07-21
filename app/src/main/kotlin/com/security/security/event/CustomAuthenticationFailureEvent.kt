package com.security.security.event

import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException

class CustomAuthenticationFailureEvent(
    authentication: Authentication,
    exception: AuthenticationException
) : AbstractAuthenticationFailureEvent(authentication, exception)
