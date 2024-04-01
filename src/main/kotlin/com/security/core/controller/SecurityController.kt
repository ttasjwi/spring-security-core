package com.security.core.controller

import com.security.core.util.getLogger
import jakarta.servlet.http.HttpSession
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.security.web.context.SecurityContextRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SecurityController(

) {

    private val log = getLogger(javaClass)

    @GetMapping("/thread")
    fun thread(): String {
        val firstThreadContext = SecurityContextHolder.getContext()
        val firstThreadAuthentication = firstThreadContext.authentication

        log.info { "firstThreadContext = $firstThreadContext"}
        log.info { "firstThreadAuthentication = $firstThreadAuthentication" }
        Thread {
            val secondThreadContext = SecurityContextHolder.getContext()
            val secondThreadAuthentication = secondThreadContext.authentication

            log.info { "secondThreadContext = $secondThreadContext" }
            log.info { "secondThreadAuthentication = $secondThreadAuthentication" }
            log.info { "isSameSecurityContext = ${firstThreadContext === secondThreadContext }" }
            log.info { "isSameAuthentication = ${firstThreadAuthentication === secondThreadAuthentication }" }
        }.start()
        return "thread"
    }

    @GetMapping("/session-security-context-repository")
    fun repo(session: HttpSession): String {
        val holderContext = SecurityContextHolder.getContext()
        val holderAuthentication = holderContext.authentication

        val sessionRepositoryContext = session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY) as SecurityContext
        val sessionAuthentication = sessionRepositoryContext.authentication


        log.info { "holderContext = $holderContext" }
        log.info { "holderAuthentication = $holderAuthentication" }
        log.info { "sessionRepositoryContext = $sessionRepositoryContext" }
        log.info { "sessionAuthentication = $sessionAuthentication" }
        log.info { "isSameContext = ${holderContext === sessionRepositoryContext}" }
        log.info { "isSameAuthentication = ${holderAuthentication === sessionAuthentication}" }

        return "session"
    }

}

