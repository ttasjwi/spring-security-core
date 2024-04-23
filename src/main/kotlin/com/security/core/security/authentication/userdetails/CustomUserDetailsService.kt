package com.security.core.security.authentication.userdetails

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class CustomUserDetailsService : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails {
        return if ("user" == username) {
            User.withUsername("user").password("{noop}1111").roles("USER").build()
        } else {
            throw UsernameNotFoundException("일치하는 username의 회원을 조회하지 못 함")
        }
    }
}
