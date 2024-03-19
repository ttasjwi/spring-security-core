package com.security.core.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            formLogin {  }
            authorizeRequests {
                authorize("/user", hasRole("USER"))
                authorize("/sys", hasRole("SYS"))
                authorize("/admin/pay", hasRole("ADMIN"))
                authorize("/admin/**", access = "hasRole('ADMIN') or hasRole('SYS')")
                authorize(anyRequest, authenticated)
            }
        }
        return http.build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val user = User.withDefaultPasswordEncoder().username("user").password("1111").roles("USER").build()
        val sys = User.withDefaultPasswordEncoder().username("sys").password("1111").roles("SYS", "USER").build()
        val admin = User.withDefaultPasswordEncoder().username("admin").password("1111").roles("ADMIN", "SYS", "USER").build()

        return InMemoryUserDetailsManager(user, sys, admin)
    }
}
