package com.security.controller

import com.security.support.logging.getLogger
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SecurityController {

    private val log = getLogger(javaClass)

    @GetMapping("/csrf")
    fun csrf(csrfToken: CsrfToken): CsrfToken {
        return csrfToken
    }

    @PostMapping("/csrf")
    fun csrf(): String {
        log.info { "csrf 검증 거침" }
        return "csrf 적용됨"
    }

    @PostMapping("/ignoreCsrf")
    fun ignoreCsrf(): String {
        log.info { "csrf 검증 무시" }
        return "csrf 무시됨"
    }
}
