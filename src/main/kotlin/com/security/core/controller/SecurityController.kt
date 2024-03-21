package com.security.core.controller

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SecurityController {

    @GetMapping("/")
    fun index(request: HttpServletRequest): String {
        val csrfToken = request.getAttribute("_csrf") as CsrfToken
        println("CSRF HeaderName = ${csrfToken.headerName}")
        println("CSRF ParamName = ${csrfToken.parameterName}")
        println("CSRF token = ${csrfToken.token}")
        return "home"
    }

    @PostMapping("/")
    fun poseIndex() = "postHome"
}
