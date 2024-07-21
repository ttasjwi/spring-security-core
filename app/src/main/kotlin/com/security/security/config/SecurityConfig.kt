package com.security.security.config

import com.security.security.authentication.CustomAuthenticationProvider
import com.security.security.event.CustomAuthenticationSuccessEvent
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val applicationContext: ApplicationContext,
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize(anyRequest, permitAll)
            }
            formLogin {
                authenticationSuccessHandler = AuthenticationSuccessHandler { request, response, authentication ->
                    applicationContext.publishEvent(CustomAuthenticationSuccessEvent(authentication))
                    response.sendRedirect("/")
                }
            }
            csrf { disable() }
        }
        return http.build()
    }

    @Bean
    fun customAuthenticationProvider(applicationEventPublisher: ApplicationEventPublisher): AuthenticationProvider {
        return CustomAuthenticationProvider(applicationEventPublisher)
    }

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer {
            it.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
        }
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val user = User.withUsername("user").password("{noop}1111").roles("USER").build()
        val db = User.withUsername("db").password("{noop}1111").roles("DB").build()
        val admin = User.withUsername("admin").password("{noop}1111").roles("ADMIN", "SECURE").build()
        return InMemoryUserDetailsManager(user, db, admin)
    }

}
