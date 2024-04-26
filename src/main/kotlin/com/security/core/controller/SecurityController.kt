package com.security.core.controller

import com.security.core.util.getLogger
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.CurrentSecurityContext
import org.springframework.security.core.context.SecurityContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SecurityController {

    private val log = getLogger(javaClass)

    @GetMapping("/")
    fun home(@CurrentSecurityContext securityContext: SecurityContext): Authentication {
        val authentication = securityContext.authentication
        log.info { "authentication = $authentication" }
        return authentication
    }

    @GetMapping("/admin")
    fun admin(): String {
        return "admin"
    }

    @GetMapping("/denied")
    fun denied(): String {
        return "denied"
    }

    // AuthenticationEntryPoint 설정 시 폼 방식 디플트 로그인 페이지가 생성되지 않아서, 커스텀하게 구현해야 함
    @GetMapping("/login")
    fun login(): String {
        return "커스텀 로그인 페이지"
    }

}
