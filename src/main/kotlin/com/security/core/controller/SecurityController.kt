package com.security.core.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SecurityController {

    @GetMapping("/")
    fun index() = "home"

    @GetMapping("/user")
    fun user() = "user"

    @GetMapping("/sys")
    fun sys() = "sys"

    @GetMapping("/admin/pay")
    fun adminPay() = "adminPay"

    @GetMapping("/admin/**")
    fun admin() = "admin"
}
