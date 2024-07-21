package com.security.adapters.input.api

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ApiInputAdapter {

    @GetMapping("/")
    fun home(authentication: Authentication?): String {
        if (authentication == null || !authentication.isAuthenticated) {
            return "get out!"
        }
        return "hello"
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    fun user(): String {
        return "user"
    }

    @PreAuthorize("hasRole('DB')")
    @GetMapping("/db")
    fun db(): String {
        return "db"
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    fun admin(): String {
        return "admin"
    }
}
