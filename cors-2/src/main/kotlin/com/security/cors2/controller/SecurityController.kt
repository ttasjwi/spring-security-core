package com.security.cors2.controller

import com.security.cors2.controller.dto.UsersListElement
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SecurityController {

    @GetMapping("/api/users")
    fun users(): ResponseEntity<List<UsersListElement>> {
        val list = listOf(UsersListElement(1L, "admin"), UsersListElement(2L, "user"))

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(list)
    }
}
