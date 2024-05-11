package com.security.controller

import com.security.support.logging.getLogger
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SecurityController {

    private val log = getLogger(javaClass)

    @GetMapping("/")
    fun index(): String {
        return "index"
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    fun admin() : String {
        log.info { "admin!" }
        return "admin"
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    fun user(): String {
        log.info { "user or admin" }
        return "user"
    }
}
