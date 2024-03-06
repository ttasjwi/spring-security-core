package com.security.core.config

import com.security.core.util.getLogger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler

@Configuration
class SecurityConfig {

    private val logger = getLogger(javaClass)

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize(anyRequest, authenticated)
            }
            formLogin { }
            logout {
                //logoutUrl = "/logout" // 로그아웃 처리 URL(POST)
                //logoutSuccessUrl = "/login" // 로그아웃 성공 후 이동 페이지(successHandler 설정을 안 하면 이쪽으로 리다이렉트 되도록 함)
                addLogoutHandler(myLogoutHandler()) // 로그아웃 핸들러
                deleteCookies("JSESSIONID", "remember-me") // 로그아웃 후 쿠키 삭제
                logoutSuccessHandler = logoutSuccessHandler() // 로그아웃 성공 후 핸들러
            }
        }
        return http.build()
    }

    private fun myLogoutHandler() = LogoutHandler { request, response, authentication ->
        val session = request.session
        session.invalidate() // 세션 무효화
        logger.info { "세션이 무효화됐습니다." }
        // 추가적인 로직을 구현하고 싶으면 여기서
    }

    private fun logoutSuccessHandler() = LogoutSuccessHandler { request, response, authentication ->
        response.sendRedirect("/login") // 로그인 페이지로 이동
        // 추가적인 로직을 구현하고 싶으면 여기서 구현
        logger.info { "로그아웃에 성공했으므로 로그인 페이지로 이동합니다." }
    }
}
