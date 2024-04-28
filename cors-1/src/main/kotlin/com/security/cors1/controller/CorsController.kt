package com.security.cors1.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class CorsController {

    @GetMapping
    fun index(): String {
        return "/index"
    }
}
