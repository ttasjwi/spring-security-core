package com.security.core.security.config

import com.security.core.security.authentication.provider.CustomAuthenticationProvider
import com.security.core.security.authentication.userdetails.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize(anyRequest, authenticated)
            }
            formLogin {  }
        }
        return http.build()
    }

    @Bean
    fun userDetailsService() : UserDetailsService {
        return CustomUserDetailsService()
    }

    @Bean
    fun customAuthenticationProvider(userDetailsService: UserDetailsService): CustomAuthenticationProvider {
        return CustomAuthenticationProvider(userDetailsService)
    }
}
