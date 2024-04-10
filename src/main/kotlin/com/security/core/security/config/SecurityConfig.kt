package com.security.core.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize("/anonymous", hasRole("GUEST"))
                authorize("/anonymousContext", permitAll)
                authorize("/authentication", permitAll)
                authorize(anyRequest, authenticated)
            }
            formLogin { }
            anonymous {
                principal = "guest"
                authorities = listOf(SimpleGrantedAuthority("ROLE_GUEST"))
            }
        }
        return http.build()
    }

}
