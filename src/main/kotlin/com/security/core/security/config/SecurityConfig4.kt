//package com.security.core.security.config
//
//import com.security.core.security.authentication.provider.CustomAuthenticationProvider
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.config.annotation.web.invoke
//import org.springframework.security.web.SecurityFilterChain
//
//@EnableWebSecurity
//@Configuration
//class SecurityConfig4 {
//
//    // CustomAuthenticationProvider를 스프링 빈으로 '2개' 등록할 경우
//    @Bean
//    fun filterChain(http: HttpSecurity): SecurityFilterChain {
//        http {
//            authorizeHttpRequests {
//                authorize(anyRequest, authenticated)
//            }
//            formLogin {  }
//        }
//        return http.build()
//    }
//
//    @Bean
//    fun customAuthenticationProvider(): CustomAuthenticationProvider {
//        return CustomAuthenticationProvider()
//    }
//
//    @Bean
//    fun customAuthenticationProvider2(): CustomAuthenticationProvider {
//        return CustomAuthenticationProvider()
//    }
//}
