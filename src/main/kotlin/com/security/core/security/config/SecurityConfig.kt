package com.security.core.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize(anyRequest, authenticated)
            }
            formLogin {  }
            sessionManagement {
                sessionConcurrency {
                    maximumSessions = 1 // 최대 허용 가능 세션수 / -1 : 무제한 로그인 세션 허용(기본값)
                    maxSessionsPreventsLogin = true // 현재 동시 로그인 차단함, false: 기존 세션 만료(default)
                    //expiredUrl = "/expired" // 세션이 만료된 경우 이동할 페이지
                }
                // 참고로 expiredUrl, invalidSessionUrl 모두 설정되면 invalidSessionUrl 이 우선시되어 이동된다.
                //invalidSessionUrl = "/invalid" // 세션이 유효하지 않을 때 이동할 페이지
            }
        }
        return http.build()
    }
}
