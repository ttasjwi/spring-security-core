package com.security.core.controller

import com.security.core.controller.dto.LoginRequest
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.context.SecurityContextRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController(
    private val authenticationManager: AuthenticationManager,
    private val securityContextRepository: SecurityContextRepository,
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest, request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val unauthenticatedToken = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username, loginRequest.password)

        val authenticatedToken = authenticationManager.authenticate(unauthenticatedToken)

        // 홀더에 저장
        val securityContext = SecurityContextHolder.getContextHolderStrategy().createEmptyContext()
        securityContext.authentication = authenticatedToken
        SecurityContextHolder.getContextHolderStrategy().context = securityContext

        // 리포지토리에 저장
        securityContextRepository.saveContext(securityContext, request, response)
        return authenticatedToken
    }
}
