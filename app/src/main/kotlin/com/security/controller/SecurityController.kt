package com.security.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SecurityController {

    @GetMapping("/")
    fun index(): String {
        return "index"
    }

    @GetMapping("/user")
    fun user(): String {
        return "user"
    }

    @GetMapping("/db")
    fun db(): String {
        return "db"
    }

    @GetMapping("/admin")
    fun admin(): String {
        return "admin"
    }

    @GetMapping("/secure")
    fun api(): String {
        return "secure"
    }
}
