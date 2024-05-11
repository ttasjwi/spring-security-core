package com.security.controller

import com.security.controller.dto.AccountDto
import org.springframework.security.access.prepost.PostAuthorize
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class SecurityController {

    @GetMapping("/")
    fun index(): String {
        return "index"
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    fun admin() : String {
        return "admin"
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    fun user(): String {
        return "user"
    }

    @GetMapping("/isAuthenticated")
    @PreAuthorize("isAuthenticated()")
    fun isAuthenticated(): String {
        return "isAuthenticated"
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("#id == authentication.name")
    fun authentication(@PathVariable(name = "id") id: String): String {
        return id
    }

    @GetMapping("/owner")
    @PostAuthorize("returnObject.owner == authentication.name")
    fun owner(name: String): AccountDto {
        return AccountDto(name, false)
    }

    @GetMapping("/isSecure")
    @PostAuthorize("hasAuthority('ROLE_ADMIN') and returnObject.isSecure")
    fun isSecure(name: String, secure: String): AccountDto {
        return AccountDto(name, "Y" == secure)
    }
}
