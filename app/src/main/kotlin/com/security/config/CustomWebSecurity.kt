package com.security.config

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component("customWebSecurity")
class CustomWebSecurity {

    fun check(authentication: Authentication, request: HttpServletRequest): Boolean {
        return authentication.isAuthenticated
    }
}
