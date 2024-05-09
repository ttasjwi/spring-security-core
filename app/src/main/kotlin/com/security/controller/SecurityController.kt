package com.security.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class SecurityController {

    @GetMapping("/")
    fun index(): String {
        return "index"
    }

    @GetMapping("/custom")
    fun custom(): String {
        return "custom"
    }

    @GetMapping("/user/{name}")
    fun userName(@PathVariable(value = "name") name: String): String {
        return name
    }

    @GetMapping("/admin/db")
    fun admin(): String {
        return "admin"
    }

}
