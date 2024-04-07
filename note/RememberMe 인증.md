<nav>
    <a href="../#api-filter" target="_blank">[Spring Security Core]</a>
</nav>


# RememberMe 인증

## RememberMe 인증이란

---

## RememberMe 설정
```kotlin
@Configuration
class SecurityConfig(
    private val userDetailsService: UserDetailsService
) {

    private val logger = getLogger(javaClass)

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize(anyRequest, authenticated)
            }
            formLogin { }
            rememberMe {
                rememberMeParameter = "remember" // 파라미터명 : 기본 파라미터명은 remember-me
                tokenValiditySeconds = 3600 // 토큰이 유효한 시간 : 초 단위 설정, 기본 값은 14일(2주)
                // alwaysRemember = true // 사용자가 리멤버 미를 활성화시키는 작업 없이도 항상 실행 (일반적으로 false)
                userDetailsService = userDetailsService() // 리멤버미 인증 작업에서 사용자 계정을 조회하는 작업 수행. 반드시 필요...
            }
        }
        return http.build()
    }

    private fun userDetailsService() = userDetailsService
}
```
- SecurityFilterChain 구성 시 RememberMe 설정을 할 수 있다.

### 결과
- 로그인 후 로그인 쿠키를 제거하더라도, 새로고침하면 다시 인증에 성공하고 로그인 쿠키가 재발급된다.

---

## RememberMeAuthenticationFilter 인증 흐름
![/imgs/remember-me-authentication.png](/imgs/remember-me-authentication.png)

- 필터에 요청이 들어오면, SecurityContextHolderStrategy 를 통해 SecurityContext를 가져오고, Authentication의 존재 여부를 확인한다.
  - Authentication이 있으면(앞에서 이미 인증이 완료됐다면) 다음 필터로 요청을 포워딩 시킨다.
- RememberMeServices의 autoLogin을 호출하여 리멤버미 인증을 시도하고 Authentication을 얻어온다.
  - 이 결과가 null 일 경우, 아무 것도 안 하고 다음 필터로 위임한다.
- Authentication을 얻어왔다면 AuthenticationManager를 통해 재인증을 시도한다.
- 여기까지 인증 과정에서 성공했다면 성공 로직을 수행
  - SecurityContextHolderStrategy를 통해 SecurityContext에 Authentication에 인증 정보를 저장한다.
  - SecurityContextRepository에 SecurityContext를 저장한다.
  - ApplicationEventPublisher를 통해 인증 성공 이벤트를 발행한다.
  - AuthenticationSuccessHandler가 있다면 인증 성공 후속처리를 별도로 진행한다.
- 일련의 과정에서 인증에 실패했다면 실패 로직을 수행
  - RememberMeServices의 loginFail 메서드 호출
  - ...

---

## RememberMeService
```java
public interface RememberMeServices {
    
	Authentication autoLogin(HttpServletRequest request, HttpServletResponse response);
    
	void loginFail(HttpServletRequest request, HttpServletResponse response);

	void loginSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication successfulAuthentication);

}
```
- 리멤버미 인증과 관련된 작업을 수행하는 역할
  - autoLogin: 요청, 응답 객체를 받아 리멤버미 인증을 수행하고 Authentication을 얻어온다.
  - loginSuccess : 로그인이 성공했을 때, 적절한 작업을 수행하게 한다.
  - loginFail: 로그인에 실패했을 때, 적절한 작업을 수행하게 한다.
- 구현체
  - NullAuthenticationServices : autoLogin에 대해서 null 을 반환하고, loginSuccess, loginFail 이 호출되도 아무 것도 안 함.
  - AbstractAuthenticationServices
    - 추상 골격 클래스.
    - TokenBasedRememberMeServices: 토큰을 브라우저에 저장해서 관리
    - PersistentTokenBasedRememberMeServices: 토큰을 서버에서 저장해서 관리

---

## RememberMe 인증에 필요한 데이터는 최초에 어디서 획득되는가?
- 이전에 우리가 학습한 UsernamePasswordAuthenticationFilter 의 상위 클래스인 AbstractAuthenticationProcessingFilter 를 기억하는가?
- 이 필터는 내부적으로 `RememberMeServices`를 의존성으로 가지고 있다.
- 로그인에 성공하면 loginSuccess를 호출하고, 로그인에 실패하면 loginFail을 호출하여 적절한 작업을 수행하게 시킨다.
  - 로그인 성공 과정에서 리멤버미 인증에 관한 토큰이 생성된다고 보면 된다.

---
