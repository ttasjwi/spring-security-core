package com.security.core.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize(HttpMethod.GET, "/", permitAll)
                authorize(anyRequest, authenticated)
            }
            formLogin {  }
            sessionManagement {
                sessionFixation {
                    changeSessionId() // 기본값
                    // 그 외 : none, migrateSession, newSession
                }
            }
        }
        return http.build()
    }

}
