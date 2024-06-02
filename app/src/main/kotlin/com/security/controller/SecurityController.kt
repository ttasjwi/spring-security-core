package com.security.controller

import com.security.domain.Account
import com.security.domain.DataService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SecurityController(
    private val dataService: DataService
) {

    @GetMapping("/user")
    fun user(): String {
        return dataService.getUser()
    }

    @GetMapping("/owner")
    fun owner(name: String): Account {
        return dataService.getOwner(name)
    }

    @GetMapping("/display")
    fun display(): String {
        return dataService.display()
    }
}
