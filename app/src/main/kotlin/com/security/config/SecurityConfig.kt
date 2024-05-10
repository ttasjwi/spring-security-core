package com.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher
import org.springframework.web.servlet.handler.HandlerMappingIntrospector

@EnableWebSecurity
@Configuration
class SecurityConfig {

    @Order(0)
    @Bean
    fun securityFilterChain1(http: HttpSecurity, introspector: HandlerMappingIntrospector): SecurityFilterChain {
        http {
            securityMatcher(MvcRequestMatcher(introspector, "/api/**"), MvcRequestMatcher(introspector, "/oauth/**"))
            authorizeHttpRequests {
                authorize(anyRequest, permitAll)
            }
        }
        return http.build()
    }


    @Order(1)
    @Bean
    fun securityFilterChain2(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize(anyRequest, authenticated)
            }
            formLogin {}
        }
        return http.build()
    }


    @Bean
    fun userDetailsService(): UserDetailsService {
        val user = User.withUsername("user").password("{noop}1111").roles("USER").build()
        val manager = User.withUsername("manager").password("{noop}1111").roles("MANAGER").build()
        val admin = User.withUsername("admin").password("{noop}1111").roles("ADMIN", "WRITE").build()
        return InMemoryUserDetailsManager(user, manager, admin)
    }
}
