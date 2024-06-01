package com.security.authorization.manager

import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.core.Authentication
import org.springframework.security.web.access.intercept.RequestAuthorizationContext
import org.springframework.security.web.access.intercept.RequestMatcherDelegatingAuthorizationManager
import org.springframework.security.web.util.matcher.RequestMatcherEntry
import java.util.function.Supplier

class CustomRequestMatcherDelegatingAuthorizationManager(
    mappings: List<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>>
): AuthorizationManager<RequestAuthorizationContext> {

    private val delegate = RequestMatcherDelegatingAuthorizationManager
        .builder()
        .mappings{ it.addAll(mappings) }
        .build()

    override fun check(
        authentication: Supplier<Authentication>,
        context: RequestAuthorizationContext
    ): AuthorizationDecision? {
        return delegate.check(authentication, context.request)
    }

}
