package com.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.csrf.CsrfTokenRepository
import org.springframework.security.web.csrf.CsrfTokenRequestHandler
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@EnableWebSecurity
@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {

        http {
            authorizeHttpRequests {
                authorize(GET,"/csrf", permitAll)
                authorize(POST,"/requireCsrf", permitAll)
                authorize(anyRequest, authenticated)
            }
            formLogin {}
            cors {
                configurationSource = corsConfigurationSource()
            }
            csrf {
                csrfTokenRequestHandler = csrfTokenRequestHandler()
                csrfTokenRepository = csrfTokenRepository()
            }
            exceptionHandling {
                accessDeniedHandler = AccessDeniedHandler { request,response, ex ->
                    response.status = 403
                    response.writer.println(ex.message)
                }
            }
        }
        return http.build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val user = User.withUsername("user").password("{noop}1111").roles("USER").build()
        return InMemoryUserDetailsManager(user)
    }

    @Bean
    fun csrfTokenRepository(): CsrfTokenRepository {
        return CookieCsrfTokenRepository()
    }

    @Bean
    fun csrfTokenRequestHandler(): CsrfTokenRequestHandler {
        val requestHandler = XorCsrfTokenRequestAttributeHandler()
        requestHandler.setCsrfRequestAttributeName(null)
        return requestHandler
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration()
        config.addAllowedOrigin("http://localhost:3000")
        config.addAllowedMethod("*")
        config.addAllowedHeader("*")
        config.allowCredentials = true
        config.maxAge = 3600L

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }

}
