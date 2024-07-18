package com.security.security.authorization

import com.security.domain.Account
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.authorization.method.MethodInvocationResult
import org.springframework.security.core.Authentication
import java.util.function.Supplier

class MyPostAuthorizationManager : AuthorizationManager<MethodInvocationResult> {

    override fun check(
        authenticationSupplier: Supplier<Authentication>,
        result: MethodInvocationResult
    ): AuthorizationDecision {

        val authentication = authenticationSupplier.get()
        val account = result.result as Account

        val isGranted = authentication !is AnonymousAuthenticationToken && account.owner == authentication.name
        return AuthorizationDecision(isGranted)
    }
}
