package com.security.core.security.config

import com.security.core.filter.CustomAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
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
        val builder = http.getSharedObject(AuthenticationManagerBuilder::class.java)
        val myAuthenticationManager = builder.build()

        val mySecurityContextRepository = DelegatingSecurityContextRepository(RequestAttributeSecurityContextRepository(), HttpSessionSecurityContextRepository())

        http {
            authorizeHttpRequests {
                authorize("/api/login", permitAll)
                authorize(anyRequest, authenticated)
            }
            authenticationManager = myAuthenticationManager
            addFilterBefore<UsernamePasswordAuthenticationFilter>(customAuthenticationFilter(myAuthenticationManager, mySecurityContextRepository))
            securityContext {
               securityContextRepository = mySecurityContextRepository
            }
        }
        return http.build()
    }

    fun customAuthenticationFilter(authenticationManager: AuthenticationManager, securityContextRepository: SecurityContextRepository): CustomAuthenticationFilter {
        return CustomAuthenticationFilter(
            requestMatcher = AntPathRequestMatcher("/api/login", HttpMethod.GET.name()),
            authenticationManager = authenticationManager,
            securityContextRepository = securityContextRepository
        )
    }

}
