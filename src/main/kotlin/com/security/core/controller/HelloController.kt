package com.security.core.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class HelloController {

    @GetMapping("/")
    fun index(): String {
        return "root"
    }

    @GetMapping("/logoutSuccess")
    fun logoutSuccess(): String {
        return "logoutSuccess"
    }
}
