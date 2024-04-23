//package com.security.core.security.config
//
//import com.security.core.security.authentication.provider.CustomAuthenticationProvider
//import com.security.core.security.authentication.provider.CustomAuthenticationProvider2
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.config.annotation.web.invoke
//import org.springframework.security.web.SecurityFilterChain
//
//@EnableWebSecurity
//@Configuration
//class SecurityConfig2 {
//
//    // SharedObject에 등록된 AuthenticationManagerBuilder에 일반 객체 AuthenticationProvider를 지정할 경우
//    @Bean
//    fun filterChain(http: HttpSecurity): SecurityFilterChain {
//        val managerBuilder = http.getSharedObject(AuthenticationManagerBuilder::class.java)
//
//        // 같은 처리를 한다.
//        managerBuilder.authenticationProvider(CustomAuthenticationProvider())
//        http.authenticationProvider(CustomAuthenticationProvider2())
//
//        http {
//            authorizeHttpRequests {
//                authorize(anyRequest, authenticated)
//            }
//            formLogin {  }
//        }
//        return http.build()
//    }
//}
