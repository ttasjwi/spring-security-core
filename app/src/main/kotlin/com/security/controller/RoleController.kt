package com.security.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@PreAuthorize("hasAuthority('ROLE_ADMIN')") // 클래스 전체 적용
class RoleController {

    @GetMapping("/userRole")
    @PreAuthorize("hasAuthority('ROLE_USER')") // 더 구체적인 것이 우선 적용됨
    fun user(): String {
        return "user"
    }

    @GetMapping("/adminRole")
    fun admin(): String {
        return "admin"
    }
}
