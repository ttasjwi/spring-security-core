package com.security.core.security.config

import com.security.core.util.getLogger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
@Configuration
class SecurityConfig(
    private val userDetailsService: UserDetailsService
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize(anyRequest, authenticated)
            }
            formLogin { }
            rememberMe {
                rememberMeParameter = "remember" // 파라미터명 : 기본 파라미터명은 remember-me
                rememberMeCookieName = "remember" // 기억하기(rememberMe) 인증을 위한 토큰을 저장하는 쿠키 이름. 기본은 "remember-me"이다.
                tokenValiditySeconds = 3600 // 토큰이 유효한 시간 : 초 단위 설정, 기본 값은 14일(2주)
                // alwaysRemember = true // 사용자가 기억하기를 활성화시키는 작업 없이도 항상 실행 (일반적으로 false)
                userDetailsService = userDetailsService() // 리멤버미 인증 작업에서 사용자 계정을 조회하는 작업 수행. 반드시 필요...
                key = "security" // 기억하기 인증을 위해 생성된 토큰을 식별하는 키 설정 -> 보통 환경설정파일에 숨겨서 관리
            }
        }
        return http.build()
    }

    private fun userDetailsService() = userDetailsService
}
