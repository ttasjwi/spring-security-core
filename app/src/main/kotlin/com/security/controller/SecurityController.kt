package com.security.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SecurityController {

    @GetMapping("/")
    fun index(): String {
        return "index"
    }

    @GetMapping("/api/photos")
    fun photos(): String {
        return "photos"
    }

    @GetMapping("/oauth/login")
    fun oauth(): String {
        return "oauthLogin"
    }

}
