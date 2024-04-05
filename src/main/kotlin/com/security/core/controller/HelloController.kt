package com.security.core.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

    @GetMapping("/")
    fun index(): String {
        return "root"
    }

    @GetMapping("/home")
    fun home(): String {
        return "home"
    }

    @GetMapping("/loginPage")
    fun login(): String {
        return "loginPage"
    }

    @GetMapping("/failed")
    fun failed(): String {
        return "failed"
    }
}
