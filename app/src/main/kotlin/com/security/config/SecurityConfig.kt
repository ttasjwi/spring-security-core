package com.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RegexRequestMatcher
import org.springframework.web.servlet.handler.HandlerMappingIntrospector

@EnableWebSecurity
@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity, introspector: HandlerMappingIntrospector): SecurityFilterChain {

        http {
            authorizeHttpRequests {
                authorize(OrRequestMatcher(AntPathRequestMatcher("/"), AntPathRequestMatcher("/login")), permitAll)
                authorize("/user", hasAuthority("ROLE_USER")) // "/user" 엔드포인트에 대해 "USER" 권한을 요구
                authorize("/mypage/**", hasRole("USER")) // "/mypage" 및 하위 디렉터리에 대해 "USER" 권한을 요구, Ant 패턴 사용
                authorize(HttpMethod.POST, "/**", hasAuthority("ROLE_WRITE")) // POST 메소드를 사용하는 모든 요청에 대해 "write" 권한을 요구
                authorize(AntPathRequestMatcher ("/manager/**"), hasAuthority("ROLE_MANAGER")) // "/manager" 및 하위 디렉터리에 대해 "MANAGER" 권한을 요구, AntPathRequestMatcher 사용.
                authorize(MvcRequestMatcher (introspector, "/admin/payment"), hasAuthority("ROLE_ADMIN")) // "/manager" 및 하위 디렉터리에 대해 "MANAGER" 권한을 요구. AntPathRequestMatcher 사용.
                authorize("/admin/**", hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")) // "/admin" 및 하위 디렉터리에 대해 "ADMIN" 또는 "MANAGER" 권한 중 하나를 요구
                authorize(RegexRequestMatcher ("/resource/[A-Za-z0-9]+", null) , hasAuthority("ROLE_MANAGER")) // 정규 표현식을 사용하여 "/resource/[A-Za-z0-9]+" 패턴에 "MANAGER" 권한을 요구
                authorize(anyRequest, authenticated) // 위에서 정의한 규칙 외의 모든 요청은 인증을 필요
            }
            formLogin {}
            csrf { disable() }
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
