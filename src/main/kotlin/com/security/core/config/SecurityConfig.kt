package com.security.core.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.savedrequest.HttpSessionRequestCache

@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            formLogin {
                authenticationSuccessHandler = authenticationSuccessHandler()
            }
            authorizeRequests {
                // 로그인 페이지에 대한 접근을 허용
                authorize("/login", permitAll)
                authorize("/user", hasRole("USER"))
                authorize("/admin/pay", hasRole("ADMIN"))
                authorize("/admin/**", access = "hasRole('ADMIN') or hasRole('SYS')")
                authorize(anyRequest, authenticated)
            }

            exceptionHandling {
                //authenticationEntryPoint = authenticationEntryPoint() // 인증 실패 시 처리
                accessDeniedHandler = accessDeniedHandler() // 인가 실패 시 처리
            }
        }
        return http.build()
    }

    private fun authenticationSuccessHandler() = AuthenticationSuccessHandler { request, response, authentication ->
        val requestCache = HttpSessionRequestCache()
        val savedRequest = requestCache.getRequest(request, response)
        response.sendRedirect(savedRequest.redirectUrl)
    }

//    private fun authenticationEntryPoint() = AuthenticationEntryPoint { request, response, authException ->
//        response.sendRedirect("/login")
//        println("인증 필요!")
//    }

    private fun accessDeniedHandler() = AccessDeniedHandler { request, response, accessDeniedException ->
        response.sendRedirect("/denied")
        println("권한 없음!")
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val user = User.withDefaultPasswordEncoder().username("user").password("1111").roles("USER").build()
        val sys = User.withDefaultPasswordEncoder().username("sys").password("1111").roles("SYS", "USER").build()
        val admin =
                User.withDefaultPasswordEncoder().username("admin").password("1111").roles("ADMIN", "SYS", "USER").build()

        return InMemoryUserDetailsManager(user, sys, admin)
    }
}
