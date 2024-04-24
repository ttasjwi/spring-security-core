<nav>
    <a href="/#authentication-persistence" target="_blank">[Spring Security Core]</a>
</nav>


# 스프링 MVC 로그인 구현
- 스프링 시큐리티 필터에 의존하는 대신 스프링 MVC 컨트롤러쪽에 로그인 기능을 노출시켜서 사용할 수 있다.
- 대신 개발자가 커스텀하게 인증 결과에 대한 후속 처리를 처리해야한다.

---

## 1. 로그인 컨트롤러

### 1.1 요청 DTO
```kotlin
package com.security.core.controller.dto

class LoginRequest(
    val username: String? = null,
    val password: String? = null
)
```
- username, password 를 바인딩하는 DTO

### 1.2 컨트롤러
```kotlin
@RestController
class LoginController(
    private val authenticationManager: AuthenticationManager,
    private val securityContextRepository: SecurityContextRepository,
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest, request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val unauthenticatedToken = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username, loginRequest.password)

        val authenticatedToken = authenticationManager.authenticate(unauthenticatedToken)

        // 홀더에 저장
        val securityContext = SecurityContextHolder.getContextHolderStrategy().createEmptyContext()
        securityContext.authentication = authenticatedToken
        SecurityContextHolder.getContextHolderStrategy().context = securityContext

        // 리포지토리에 저장
        securityContextRepository.saveContext(securityContext, request, response)
        return authenticatedToken
    }
}
```
- AuthenticationManager 및 SecurityContextRepository를 의존성으로 주입받는다.
- AuthenticationManager에 인증을 위임하고, 인증 결과를 SecurityContextHolder 및 SecurityContextRepository에 저장한다.

---

## 2. 설정 구성
```kotlin
@EnableWebSecurity
@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize("/login", permitAll)
                authorize(anyRequest, authenticated)
            }
            csrf { disable() }
            securityContext {
                securityContextRepository = securityContextRepository()
            }
        }
        return http.build()
    }


    @Bean
    fun userDetailsService(): UserDetailsService {
        val user = User.withUsername("user").password("{noop}1111").roles("USER").build()
        return InMemoryUserDetailsManager(user)
    }

    @Bean
    fun securityContextRepository(): SecurityContextRepository {
        return DelegatingSecurityContextRepository(HttpSessionSecurityContextRepository(), RequestAttributeSecurityContextRepository())
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }
}
```
- 스프링 시큐리티는 기본적으로 CsrfFilter를 활성화시키는데, 편의상 csrf를 무효화시키켰다.
- AuthenticationManager 및 SecurityContextRepository를 스프링 빈으로 등록시켰다.

---

## 3. http 파일
```text
### 로그인
POST http://localhost:8080/login
Content-Type: application/json

{
  "username": "user",
  "password": "1111"
}

> {%
client.global.set("JSESSIONID", response.headers.valueOf("Set-Cookie").split(";")[0].split("=")[1])
%}


### 루트 접속
GET http://localhost:8080/
Accept: application/json
Cookie: JSESSIONID={{JSESSIONID}}
```
- resources 아래에 login.http 파일을 작성하자. 인텔리제이에서 편리하게 사용할 수 있다.
- 응답을 파싱해서 JSESSIONID 전역변수에 저장하고 재사용할 수 있게 했다.
  - 참고자료: https://jojoldu.tistory.com/366
- 이를 실행시켜서 잘 작동하는 것을 확인할 수 있다.

---

## 4. 한계
- 로그인이라는 주요 비즈니스 로직을 담당하는 코드에 스프링 시큐리티 기술 코드이 녹아들게 됨
- 향후 스프링 시큐리티 기술을 사용하지 않을 경우 변경에 유연하지 못 하다는 문제가 있다

---
