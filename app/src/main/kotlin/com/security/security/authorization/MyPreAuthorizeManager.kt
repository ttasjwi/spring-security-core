package com.security.security.authorization

import org.aopalliance.intercept.MethodInvocation
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.core.Authentication
import java.util.function.Supplier

class MyPreAuthorizeManager : AuthorizationManager<MethodInvocation> {

    override fun check(authenticationSupplier: Supplier<Authentication>, methodInvocation: MethodInvocation): AuthorizationDecision? {
        val authentication = authenticationSupplier.get()
        val authenticated = authentication !is AnonymousAuthenticationToken && authentication.isAuthenticated
        return AuthorizationDecision(authenticated)
    }

}
