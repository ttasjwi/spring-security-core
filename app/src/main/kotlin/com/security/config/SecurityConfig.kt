package com.security.config

import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.expression.DefaultHttpSecurityExpressionHandler
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager

@EnableWebSecurity
@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {

        http {
            authorizeHttpRequests {

                // 패턴에서 {} 영역을 변수로 사용하여 표현식에 적용할 수 있다.
                authorize("/user/{name}", WebExpressionAuthorizationManager("#name == authentication.name"))

                // 여러 개의 권한 규칙을 조합할 수 있다
                authorize("/admin/db", WebExpressionAuthorizationManager("hasAuthority('ROLE_DB') or hasRole('ADMIN')"))

                authorize(anyRequest, authenticated)
            }
            formLogin {}
            csrf { disable() }
        }
        return http.build()
    }

//    @Bean
//    fun securityFilterChain(http: HttpSecurity, context: ApplicationContext): SecurityFilterChain {
//        val expressionHandler = DefaultHttpSecurityExpressionHandler()
//        expressionHandler.setApplicationContext(context)
//
//        val webExpressionAuthorizationManager = WebExpressionAuthorizationManager("@customWebSecurity.check(authentication, request)")
//        webExpressionAuthorizationManager.setExpressionHandler(expressionHandler)
//
//        http {
//            authorizeHttpRequests {
//                // 커스텀 표현식 인가 관리자를 통하여 인가처리
//                authorize("/custom/**", webExpressionAuthorizationManager)
//                authorize(anyRequest, authenticated)
//            }
//            formLogin {}
//            csrf { disable() }
//        }
//        return http.build()
//    }

//    @Bean
//    fun securityFilterChain(http: HttpSecurity, context: ApplicationContext): SecurityFilterChain {
//        http {
//            authorizeHttpRequests {
//
//                // 커스텀 RequestMatcher 에 매치되는 것에 대하여 지정된 규칙에 따라 인가처리
//                authorize(CustomRequestMatcher("/admin"), hasAuthority("ROLE_ADMIN"))
//                authorize(anyRequest, authenticated)
//            }
//            formLogin {}
//            csrf { disable() }
//        }
//        return http.build()
//    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val user = User.withUsername("user").password("{noop}1111").roles("USER").build()
        val manager = User.withUsername("manager").password("{noop}1111").roles("MANAGER").build()
        val admin = User.withUsername("admin").password("{noop}1111").roles("ADMIN", "WRITE").build()
        return InMemoryUserDetailsManager(user, manager, admin)
    }
}
