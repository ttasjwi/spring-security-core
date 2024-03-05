package com.security.core.config

import com.security.core.util.getLogger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

@Configuration
class SecurityConfig {

    private val logger = getLogger(javaClass)

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize(anyRequest, authenticated)
            }
            formLogin {

                // loginPage = "/loginPage"
                // failureUrl = "/login
                loginProcessingUrl = "/login_proc"

                authenticationSuccessHandler = AuthenticationSuccessHandler { request, response, authentication ->
                    logger.info { "인증 성공! >> authentication = ${authentication.name}"}
                    response.sendRedirect("/")
                }

                authenticationFailureHandler = AuthenticationFailureHandler { request, response, exception ->
                    logger.info (exception) { "인증 실패" }
                    response.sendRedirect("/login")
                }

                permitAll()
            }
        }
        return http.build()
    }
}
