package com.security.controller

import com.security.domain.Account
import com.security.security.annotation.IsAdmin
import com.security.security.annotation.OwnerShip
import jakarta.annotation.security.DenyAll
import jakarta.annotation.security.PermitAll
import jakarta.annotation.security.RolesAllowed
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SecurityController {

    @GetMapping("/")
    fun index(): String {
        return "index"
    }

    @GetMapping("/user")
    @Secured("ROLE_USER")
    fun user(): String {
        return "user"
    }

    @GetMapping("/admin")
    @RolesAllowed("ADMIN")
    fun admin(): String {
        return "admin"
    }

    @GetMapping("/permitAll")
    @PermitAll
    fun permitAll(): String {
        return "permitAll"
    }

    @GetMapping("/denyAll")
    @DenyAll
    fun denyAll(): String {
        return "denyAll"
    }

    @GetMapping("/isAdmin")
    @IsAdmin
    fun isAdmin(): String {
        return "isAdmin"
    }

    @GetMapping("/ownership")
    @OwnerShip
    fun ownerShip(@RequestParam(defaultValue = "") name: String): Account {
        return Account(name, false)
    }

    @GetMapping("/delete")
    @PreAuthorize("@myAuthorizer.isUser(#operations)") // 빈,이름을,참조하고,접근,제어,로직을,수행한다
    fun delete(): String {
        return "delete"
    }
}
