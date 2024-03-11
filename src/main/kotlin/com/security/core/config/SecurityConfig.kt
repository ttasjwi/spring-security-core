package com.security.core.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize(HttpMethod.GET, "/", permitAll)
                authorize(anyRequest, authenticated)
            }
            formLogin { }
            sessionManagement {
                sessionFixation {
                    sessionFixation {
                        changeSessionId() // 기본값
                        // none()
                        // migrateSession()
                        // newSession()
                    }
                }
            }
        }
        return http.build()
    }
}
