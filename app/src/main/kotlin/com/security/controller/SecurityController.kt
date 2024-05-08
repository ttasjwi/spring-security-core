package com.security.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SecurityController {

    @GetMapping("/user")
    fun user(): String {
        return "user"
    }

    @GetMapping("/myPage/points")
    fun myPage(): String {
        return "myPage"
    }

    @GetMapping("/manager")
    fun manager(): String {
        return "manager"
    }

    @GetMapping("/admin")
    fun admin(): String {
        return "admin"
    }

    @GetMapping("/admin/payment")
    fun adminPayment(): String {
        return "adminPayment"
    }

    @GetMapping("/resource/address_01")
    fun address_01(): String {
        return "address_01"
    }

    @GetMapping("/resource/address01")
    fun address01(): String {
        return "address01"
    }

    @PostMapping("/post")
    fun post(): String {
        return "post"
    }
}
