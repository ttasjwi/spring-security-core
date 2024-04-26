package com.security.core.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler

@EnableWebSecurity
@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize("/login", permitAll)
                authorize("/admin", hasRole("ADMIN"))
                authorize(anyRequest, authenticated)
            }
            formLogin {  }
            exceptionHandling {
                // 커스텀하게 사용할 AuthenticationEntryPoint 를 지정
                authenticationEntryPoint = AuthenticationEntryPoint {request, response, authenticationException ->
                    println("${authenticationException.message}")
                    response.sendRedirect("/login")
                }

                // 커스텀하게 사용할 AccessDeniedHandler 를 지정
                accessDeniedHandler = AccessDeniedHandler {request, response, accessDeniedException ->
                    println("${accessDeniedException.message}")
                    response.sendRedirect("/denied")
                }
            }
        }
        return http.build()
    }

}
