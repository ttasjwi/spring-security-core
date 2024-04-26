<nav>
    <a href="../../#exception-handling" target="_blank">[Spring Security Core]</a>
</nav>


# 스프링 시큐리티 예외 및 처리 API

---

## 1. 개요
- 스프링 시큐리티의 필터체인에서는 인증 예외 또는 인가 예외가 발생한다.
- 인증 예외, 인가 예외를 처리하는 컴포넌트가 필요한데 예외 발생 지점에 따라 처리 방식이 달라진다.
  - ExceptionTranslationFilter 이후에 발생한 예외
    - 실질적으로 최종 인가를 담당하는 AuthorizationFilter에서 발생한 예외를 말한다.
    - 이 챕터에서는 이 부분에 초점을 다룬다.
  - ExceptionTranslationFilter 이전에 발생한 예외
    - 이 경우는 보통 각 필터에서 자체적으로 처리하거나 별도의 컴포넌트쪽(AuthenticationFailureHandler, AuthenticationEntryPoint, AccessDeniedHandler)에 위임시키는 경우가 많다.
    - 이 부분은 앞의 여러 필터들을 학습하면서 배운 것들이 많으므로 생략한다.

---

## 2. 예외 처리 유형

### 2.1 AuthenticationException
- 스프링 시큐리티 필터체인에서 인증에 실패했을 때 발생하는 추상 예외
- 주 발생위치: AbstractAuthenticationProcessingFilter (로그인 과정), RemembermeAuthenticationFilter(리멤버미 인증 관정)
- 구현체 예시
    - UsernameNotFoundException
    - InvalidCookieException
    - RememberMeAuthenticationException
    - BadCredentialsException : 자격 증명이 잘못됐을 때

### 1.2 인가 예외(AccessDeniedException)
- 주로 권한 부족 등의 이유로 발생하는 예외
- 주 발생 위치 : AuthorizationFilter(최종 인가 과정), CsrfFilter(Csrf 검증 과정)
- ExceptionTranslationFilter에서 이 예외가 감지됐을 때
  - 사용자가 익명사용자이거나 리멤버미 인증일 경우 인증 예외로 변환해서, 인증예외로 취급되어 처리된다.
  - 사용자가 익명사용자가 아니고, 리멤버미 인증이 아닐 경우 인가 예외로서 취급되어 처리된다.
  - 이 부분은 뒤의 필터쪽에서 다루도록 하자.

---

## 3. 인증/인가 예외 처리자
- 인증 예외에 대하여 커스텀한 후속처리를 하고 싶을 때 AuthenticationEntryPoint 인터페이스를 구현하여 설정하면 된다.
- 인가 예외에 대하여 커스텀한 후속처리를 하고 싶을 때 AccessDeniedHandler 인터페이스를 구현하여 설정하면 된다.

---

## 4. exceptionHandling() API
```kotlin
@Bean
fun filterChain(http: HttpSecurity): SecurityFilterChain {
    http {
        exceptionHandling {
            // 커스텀하게 사용할 AuthenticationEntryPoint 를 지정
            authenticationEntryPoint = AuthenticationEntryPoint {request, response, authenticationException ->
                println("${authenticationException.message}")
                response.sendRedirect("/login")
            }

            // 커스텀하게 사용할 AccessDeniedHandler 를 지정
            accessDeniedHandler = AccessDeniedHandler {request, response, accessDeniedException ->
                println("${accessDeniedException.message}")
                response.sendRedirect("/denied")
            }
        }
    }
    return http.build()
}
```
- AuthenticationEntryPoint, AccessDeniedHandler를 설정 가능하다.
- AuthenticationEntryPoint 는 인증 프로세스마다 기본적으로 제공되는 클래스들이 설정된다.
  - 사용자 정의 AuthenticationEntryPoint 설정 시, 가장 우선적으로 수행된다. 폼 로그인 방식 사용 시 기본 로그인 페이지 생성이 무시된다. 
  - Form 인증 방식 - LoginUrlAuthenticationEntryPoint
  - Basic 인증 방식 - BasicAuthenticationEntryPoint
  - 아무런 인증 방식이 설정되지 않으면 기본적으로 Http403ForbiddenEntryPoint가 사용된다.
- AccessDeniedHandler 는 기본적으로 AccessDeniedHandlerImple 클래스가 적용된다.

---

## 5. 실습

### 5.1 컨트롤러
```kotlin
@RestController
class SecurityController {

    private val log = getLogger(javaClass)

    @GetMapping("/")
    fun home(@CurrentSecurityContext securityContext: SecurityContext): Authentication {
        val authentication = securityContext.authentication
        log.info { "authentication = $authentication" }
        return authentication
    }

    @GetMapping("/admin")
    fun admin(): String {
        return "admin"
    }

    @GetMapping("/denied")
    fun denied(): String {
        return "denied"
    }

    // AuthenticationEntryPoint 설정 시 폼 방식 디플트 로그인 페이지가 생성되지 않아서, 커스텀하게 구현해야 함
    @GetMapping("/login")
    fun login(): String {
        return "커스텀 로그인 페이지"
    }

}
```
- `/admin` : user 권한 사용자가 접근할 때 인가 예외가 발생하는 지 테스트하기 위함
- `/denied` : 인가 예외 후속처리 결과 이동되는지 테스트하기 위함
- `/login` : 커스텀 AuthenticationEntryPoint 설정 시 디폴트 로그인 페이지가 생성되지 않아서 직접 구현해줘야한다.

### 5.2 설정
```kotlin
@EnableWebSecurity
@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize("/login", permitAll)
                authorize("/admin", hasRole("ADMIN"))
                authorize(anyRequest, authenticated)
            }
            formLogin {  }
            exceptionHandling {
                // 커스텀하게 사용할 AuthenticationEntryPoint 를 지정
                authenticationEntryPoint = AuthenticationEntryPoint {request, response, authenticationException ->
                    println("${authenticationException.message}")
                    response.sendRedirect("/login")
                }

                // 커스텀하게 사용할 AccessDeniedHandler 를 지정
                accessDeniedHandler = AccessDeniedHandler {request, response, accessDeniedException ->
                    println("${accessDeniedException.message}")
                    response.sendRedirect("/denied")
                }
            }
        }
        return http.build()
    }

}
```
- 권한 설정
  - /login 페이지는 모든 사용자가 접근가능하도록 하기
  - /admin 페이지는 admin 사용자만 접근 가능하도록 하기
  - 그 외 모든 엔드포인트는 인증을 필요로 하기
- 폼 로그인 활성화
- 인증 테스트 : 홈으로 접속 시도할 때 login 페이지로 리다이렉트 되는 지 테스트할 것
- 인가 테스트 : 폼 로그인을 해야하므로 authenticationEntryPoint 설정을 주석처리하고 실행할 것

---
