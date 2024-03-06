<nav>
    <a href="../#api-filter" target="_blank">[Spring Security Core]</a>
</nav>

# 로그아웃 처리, Logout Filter
```kotlin
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
```
- 폼 로그인을 활성화하면 로그아웃 기능이 동작한다.
- 기본설정만으로도 충분하긴 한데, 우리가 부가적인 설정을 할 수도 있다.


## 로그아웃 필터
![logout-filter](/imgs/logout-filter.png)

- 로그아웃 로직을 수행하는 필터
- 요청 가로채기 : RequestMatcher를 통해 로그아웃 요청인지 확인한뒤, 요청이 로그아웃 요청이면 가로챈다.
  - 로그아웃 요청이 아닐 경우 doFilter 호출해서 다음 필터로 요청 포워딩
- 로그아웃 처리: LogoutHandler에게 실질적 로그아웃 처리를 위임(아래에서 설명)
- 로그아웃 성공처리: LogoutSuccessHandler에게 로그아웃 성공처리를 위임
  - 기본 구현체는 SimpleUrlLogoutSuccessHandler 인데, 로그아웃 성공 URL로 리다이렉트 시킨다.
  - 우리가 LogoutSuccessHandler를 등록하면 그것이 우선적으로 실행된다.

### LogoutHandler

![img.png](/imgs/logout-handler.png)

- 로그아웃 필터는 자신이 가진 LogoutHandler에게 실질적 로그아웃 처리를 위임한다고 했었다.
- LogoutHandler의 기본 구현체는 CompositeLogoutHandler인데, 이 핸들러는 내부적으로 다른 LogoutHandler 구현체들을 리스트로 가지고 있다.
- 리스트로 가지고 있는 LogoutHandler들을 차례로 호출하면서 로그아웃 과정에서 수행해야할 처리들을 위임한다.
- 대표적인 구현체
  - CsrfLogoutHandler : Csrf 관련 로그아웃 처리
  - SecurityContextLogoutHandler: SecurityContext 관련 로그아웃 처리
    - SecurityContextHolderStrategy
    - SecurityContextRepository
  - LogoutSuccessEventPublishingLogoutHandler: 로그아웃 성공 관련 이벤트 발행

---
