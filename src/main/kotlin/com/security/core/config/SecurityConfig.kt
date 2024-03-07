package com.security.core.config

import com.security.core.util.getLogger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig(
    private val userDetailsService: UserDetailsService
) {

    private val logger = getLogger(javaClass)

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize(anyRequest, authenticated)
            }
            formLogin { }
            rememberMe {
                rememberMeParameter = "remember" // 파라미터명 : 기본 파라미터명은 remember-me
                tokenValiditySeconds = 3600 // 토큰이 유효한 시간 : 초 단위 설정, 기본 값은 14일(2주)
                // alwaysRemember = true // 사용자가 리멤버 미를 활성화시키는 작업 없이도 항상 실행 (일반적으로 false)
                userDetailsService = userDetailsService() // 리멤버미 인증 작업에서 사용자 계정을 조회하는 작업 수행. 반드시 필요...
            }
        }
        return http.build()
    }

    private fun userDetailsService() = userDetailsService
}
