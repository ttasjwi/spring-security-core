package com.security.core.controller

import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.CurrentSecurityContext
import org.springframework.security.core.context.SecurityContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class HelloController {

    @GetMapping("/")
    fun index(): String {
        return "root"
    }

    @GetMapping("/authentication")
    fun authentication(authentication: Authentication?): String {
        // 스프링 MVC가 HttpServletRequest 를 사용하여 파라미터를 해결하려 시도
        return if (authentication is AnonymousAuthenticationToken) {
            "anonymous"
        } else {
            // 익명사용자일 경우, 여기서 전달되는 Authentication은 null이다.
            "null"
        }
    }

    @GetMapping("/anonymousContext")
    fun anonymousContext(@CurrentSecurityContext context: SecurityContext): String {
        // SecurityCOntext를 얻어옴. 이 과정에서 CurrentSecurityContextArgumentResolver가 요청을 가로채서 처리한다.
        return context.authentication.name
    }
}
