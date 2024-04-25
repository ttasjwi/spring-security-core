package com.security.core.controller

import jakarta.servlet.http.HttpSession
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SecurityController {

    @GetMapping("/")
    fun index(session: HttpSession) : String {
        println("=======================")
        println("/ 로 접속")
        println("Session id = ${session.id}")
        println("Session = $session")
        return "home"
    }

    @GetMapping("/hello")
    fun hello(session: HttpSession) : String {
        println("=======================")
        println("/hello 로 접속")
        println("Session id = ${session.id}")
        println("Session = $session")
        return "hello"
    }
}
