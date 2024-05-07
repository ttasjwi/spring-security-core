package com.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.session.MapSession
import org.springframework.session.MapSessionRepository
import org.springframework.session.SessionRepository
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession
import org.springframework.session.web.http.CookieSerializer
import org.springframework.session.web.http.DefaultCookieSerializer
import java.util.concurrent.ConcurrentHashMap

@Configuration
@EnableSpringHttpSession
class SessionConfig {

    @Bean
    fun cookieSerializer(): CookieSerializer {
        val serializer = DefaultCookieSerializer()
        serializer.setUseHttpOnlyCookie(true) // 브라우저에서 쿠키에 접근할 수 없게 함
        serializer.setUseSecureCookie(true) // HTTPS가 아닌 통신에서는 쿠키를 전송하지 않음(localhost 제외)

        serializer.setSameSite("Strict") // SameSite 속성을 None으로 지정 => HTTPS 필수

        return serializer
    }

    @Bean
    fun sessionRepository(): SessionRepository<MapSession> {
        return MapSessionRepository(ConcurrentHashMap())
    }
}
