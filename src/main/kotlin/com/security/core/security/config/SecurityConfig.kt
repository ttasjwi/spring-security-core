package com.security.core.security.config

import com.security.core.security.authentication.filter.CustomAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.context.DelegatingSecurityContextRepository
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository
import org.springframework.security.web.context.SecurityContextRepository
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@EnableWebSecurity
@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        val authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder::class.java)
        val sharedAuthenticationManager = authenticationManagerBuilder.build()

        http {
            authorizeHttpRequests {
                authorize("/api/login", permitAll)
                authorize(anyRequest, authenticated)
            }
            formLogin {  }

            authenticationManager = sharedAuthenticationManager

            securityContext {
                securityContextRepository = securityContextRepository()

                // true -> holderfilter
                // false -> persistenceFilter (deprecated)
                requireExplicitSave = true
            }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(customAuthenticationFilter(sharedAuthenticationManager))
        }
        return http.build()
    }

    fun customAuthenticationFilter(authenticationManager: AuthenticationManager): CustomAuthenticationFilter {
        return CustomAuthenticationFilter(
            requestMatcher = AntPathRequestMatcher("/api/login", HttpMethod.GET.name()),
            authenticationManager = authenticationManager,
            securityContextRepository = securityContextRepository()
        )
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val user = User.withUsername("user").password("{noop}1111").roles("USER").build()
        return InMemoryUserDetailsManager(user)
    }

    @Bean
    fun securityContextRepository(): SecurityContextRepository {
        return DelegatingSecurityContextRepository(HttpSessionSecurityContextRepository(), RequestAttributeSecurityContextRepository())
    }
}
