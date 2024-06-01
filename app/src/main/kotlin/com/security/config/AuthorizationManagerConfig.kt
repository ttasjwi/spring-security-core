package com.security.config

import com.security.authorization.manager.CustomRequestMatcherDelegatingAuthorizationManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authorization.AuthenticatedAuthorizationManager
import org.springframework.security.authorization.AuthorityAuthorizationManager
import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.web.access.intercept.RequestAuthorizationContext
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher
import org.springframework.security.web.util.matcher.AnyRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcherEntry
import org.springframework.web.servlet.handler.HandlerMappingIntrospector


@Configuration
class AuthorizationManagerConfig {

    @Bean(name= ["customAuthorizationManager"])
    fun customAuthorizationManager(introspector: HandlerMappingIntrospector): AuthorizationManager<RequestAuthorizationContext> {
        val mappings = listOf<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>>(
            RequestMatcherEntry(
                MvcRequestMatcher(introspector, "/user"),
                AuthorityAuthorizationManager.hasAuthority("ROLE_USER")
            ),
            RequestMatcherEntry(
                MvcRequestMatcher(introspector, "/db"),
                AuthorityAuthorizationManager.hasAuthority("ROLE_DB")
            ),
            RequestMatcherEntry(
                MvcRequestMatcher(introspector, "/admin"),
                AuthorityAuthorizationManager.hasRole("ADMIN")
            ),
            RequestMatcherEntry(
                AnyRequestMatcher.INSTANCE,
                AuthenticatedAuthorizationManager()
            )
        )
        return CustomRequestMatcherDelegatingAuthorizationManager(mappings)
    }
}
