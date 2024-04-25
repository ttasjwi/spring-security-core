package com.security.core.controller

import com.security.core.util.getLogger
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.CurrentSecurityContext
import org.springframework.security.core.context.SecurityContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class HelloController {

    private val log = getLogger(javaClass)

    @GetMapping("/")
    fun home(@CurrentSecurityContext securityContext: SecurityContext): Authentication {
        val authentication = securityContext.authentication
        log.info { "authentication = $authentication" }
        return authentication
    }

}
