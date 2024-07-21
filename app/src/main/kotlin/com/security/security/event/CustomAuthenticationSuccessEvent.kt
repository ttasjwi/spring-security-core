package com.security.security.event

import org.springframework.security.authentication.event.AbstractAuthenticationEvent
import org.springframework.security.core.Authentication

class CustomAuthenticationSuccessEvent(
    authentication: Authentication
) : AbstractAuthenticationEvent(authentication)
