package com.security.core.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.savedrequest.HttpSessionRequestCache

@EnableWebSecurity
@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        val customRequestCache = HttpSessionRequestCache()
        customRequestCache.setMatchingRequestParameterName("customParam=y") // 특정 파라미터

        http {
            authorizeHttpRequests {
                authorize("/logoutSuccess", permitAll)
                authorize(anyRequest, authenticated)
            }
            formLogin {}
            requestCache {
                requestCache = customRequestCache
                // requestCache = NullRequestCache() : 요청을 저장하지 않고 싶을 때
            }
        }
        return http.build()
    }

}
