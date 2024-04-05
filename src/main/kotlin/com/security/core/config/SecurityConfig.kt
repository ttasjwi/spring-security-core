package com.security.core.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

@EnableWebSecurity
@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize(anyRequest, authenticated)
            }
        }
        http.formLogin { form ->
            form
                .loginPage("/loginPage")
                .loginProcessingUrl("/loginProc")
                .defaultSuccessUrl("/", true)
                .failureUrl("/failed")
                .usernameParameter("userId")
                .passwordParameter("passwd")
                .successHandler { request, response, authentication ->
                    println("authentication: ${authentication.name}")
                    response.sendRedirect("/home")
                }
                .failureHandler { request, response, authenticationException ->
                    println("exception: ${authenticationException.message}")
                    response.sendRedirect("/login")
                }
                .permitAll()
        }
        return http.build()
    }
}
