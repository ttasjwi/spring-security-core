package com.security.authorization.manager

import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.core.Authentication
import org.springframework.security.web.access.intercept.RequestAuthorizationContext
import java.util.function.Supplier

class CustomAuthorizationManager : AuthorizationManager<RequestAuthorizationContext> {

    companion object {
        const val REQUIRED_ROLE = "ROLE_SECURE"
    }

    override fun check(
        supplier: Supplier<Authentication?>,
        `object`: RequestAuthorizationContext?

    ): AuthorizationDecision {
        val authentication = supplier.get()

        return AuthorizationDecision(
            authentication !== null
                    && authentication.isAuthenticated
                    && authentication !is AnonymousAuthenticationToken
                    && authentication.authorities.any { REQUIRED_ROLE == it.authority }
        )
    }

}
