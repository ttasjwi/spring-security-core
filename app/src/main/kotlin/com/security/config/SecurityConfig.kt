package com.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.core.GrantedAuthorityDefaults
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain


@EnableWebSecurity
@Configuration
class SecurityConfig {

//    @Bean
//    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
//        http {
//            authorizeHttpRequests {
//                authorize("/user", hasRole("USER"))
//                authorize("/admin", hasRole("ADMIN"))
//                authorize("/db", hasRole("DB"))
//                authorize(anyRequest, authenticated)
//            }
//            formLogin { }
//        }
//        return http.build()
//    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests {
                it
                    .requestMatchers("/user").hasRole("USER")
                    .requestMatchers("/admin").hasRole("ADMIN")
                    .requestMatchers("/db").hasRole("DB")
                    .anyRequest().authenticated()
            }
            .formLogin(Customizer.withDefaults())
        return http.build()
    }

    @Bean
    fun grantedAuthorityDefaults(): GrantedAuthorityDefaults {
        return GrantedAuthorityDefaults("MYPREFIX_")
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val user = User.withUsername("user").password("{noop}1111").authorities("MYPREFIX_USER").build()
        val db = User.withUsername("db").password("{noop}1111").authorities("MYPREFIX_DB").build()
        val admin = User.withUsername("admin").password("{noop}1111").authorities("MYPREFIX_ADMIN").build()
        return InMemoryUserDetailsManager(user, db, admin)
    }
}
