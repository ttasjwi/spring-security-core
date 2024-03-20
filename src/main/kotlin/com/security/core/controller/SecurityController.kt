package com.security.core.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SecurityController {

    @GetMapping("/")
    fun index() = "home"

    @GetMapping("/login")
    fun login() = "login"

    @GetMapping("/denied")
    fun denied() = "denied"
}
