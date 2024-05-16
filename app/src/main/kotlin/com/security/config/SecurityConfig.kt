package com.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain


@EnableWebSecurity
@Configuration
class SecurityConfig {

//    @Bean
//    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
//        http {
//            authorizeHttpRequests {
//                authorize("/user", hasRole("USER"))
//                authorize("/admin", hasRole("ADMIN"))
//                authorize("/db", hasRole("DB"))
//                authorize(anyRequest, authenticated)
//            }
//            formLogin { }
//            csrf { disable() }
//        }
//        return http.build()
//    }
//
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests {
            it
                .requestMatchers("/user").hasRole("USER")
                .requestMatchers("/admin").hasRole("ADMIN")
                .requestMatchers("/db").hasRole("DB")
        }
            .formLogin(Customizer.withDefaults())
            .csrf { it.disable() }
        return http.build()
    }


    @Bean
    fun userDetailsService(): UserDetailsService {
        val user = User.withUsername("user").password("{noop}1111").roles("USER").build()
        val db = User.withUsername("db").password("{noop}1111").roles("DB").build()
        val admin = User.withUsername("admin").password("{noop}1111").roles("ADMIN", "SECURE").build()
        return InMemoryUserDetailsManager(user, db, admin)
    }

    @Bean
    fun roleHierarchy(): RoleHierarchy {
        val roleHierarchy = RoleHierarchyImpl()
        roleHierarchy.setHierarchy(
            """
            ROLE_ADMIN > ROLE_DB
            ROLE_DB > ROLE_USER
            ROLE_USER > ROLE_ANONYMOUS
        """.trimIndent()
        )
        return roleHierarchy
    }
}
