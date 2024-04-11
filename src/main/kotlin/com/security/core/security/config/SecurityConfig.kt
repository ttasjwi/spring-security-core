package com.security.core.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@EnableWebSecurity
@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize("/logoutSuccess", permitAll)
                authorize(anyRequest, authenticated)
            }
            // csrf { disable() }
            logout {
                logoutUrl = "/logout" // 로그아웃 처리 URL(POST)
                logoutRequestMatcher = AntPathRequestMatcher("/logout", "POST")
                logoutSuccessUrl = "/login" // 로그아웃 성공 후 이동 페이지(successHandler 설정을 안 하면 이쪽으로 리다이렉트 되도록 함)
                addLogoutHandler(myLogoutHandler()) // 로그아웃 핸들러
                deleteCookies("JSESSIONID", "remember-me") // 로그아웃 후 쿠키 삭제
                invalidateHttpSession = true // HttpSession을 무효화해야하는 경우. true(기본값) , 그렇지 않을 경우 false
                clearAuthentication = true // 로그아웃 시 SecurityContextLogoutHandler가 인증(Authentication)을 삭제해야 하는지 여부를 명시
                logoutSuccessHandler = logoutSuccessHandler() // 로그아웃 성공 후 핸들러
                permitAll = true // logoutUrl, RequestMatcher의 URL에 대해 모든 사용자의 접근 허용
            }
        }
        return http.build()
    }

    @Bean
    fun myLogoutHandler(): LogoutHandler {
        return LogoutHandler {request, response, authentication ->
            val session = request.session
            session?.invalidate()
            SecurityContextHolder.getContextHolderStrategy().context.authentication = null
            SecurityContextHolder.getContextHolderStrategy().clearContext()
        }
    }

    @Bean
    fun logoutSuccessHandler(): LogoutSuccessHandler {
        return LogoutSuccessHandler {request, response, authentication ->
            response.sendRedirect("/logoutSuccess")
        }
    }
}
