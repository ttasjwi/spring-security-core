<nav>
    <a href="../#api-filter" target="_blank">[Spring Security Core]</a>
</nav>

# 예외 처리 및 요청 캐시 필터 - ExceptionTranslationFilter, RequestCacheAwareFilter

---

## 1. ExceptionTranslationFilter
### 1.1 위치
- SecurityFilterChain 제일 끝에 위치한 필터는 `AuthorizationFilter` 이다.
  - Spring Security 5.5 이전까지는 FilterSecurityInterceptor 였다.
- ExceptionTranslationFilter는 AuthorizationFilter 바로 앞에 위치해있다.
- **따라서 ExceptionTranslationFilter는 AuthorizationFilter에서 발생한 인증 예외, 인가 예외를 처리해주는 필터라고 보면 된다.**

### 1.2 인증 예외(AuthenticationException) 처리
1. 인증예외 발생 이유 
   - ExceptionTranslationFilter에서 직접적으로 발생한 AuthenticationExcepition
   - 아래에서 후술할 익명사용자/RememberMe 사용자의 인가 예외가 변환된 경우 
2. AuthenticationEntryPoint 호출
   - 로그인 페이지 이동, 401 오류 코드 전달 등
   - AuthenticationEntryPoint 는 인증 예외의 후속 처리를 담당한다. 
3. SecurityContext 비우기 
4. 인증 예외가 발생하기 전의 요청 정보를 저장
   - `RequestCache` - 사용자의 이전 요청 정보을 세션에 저장하고 이를 꺼내 오는 캐시 메커니즘을 수행하는 역할
     - 인터페이스다.
     - 메서드 : `saveRequest` , `getRequest` , …
   - `SavedRequest`- 사용자가 요청했던 request 파라미터 값들, 그 당시의 헤더값들 등을 저장한 형태
     - 이것도 인터페이스다.
     - 캐싱한 요청 정보를 얻어올 때 이 인터페이스로 얻어와진다.
     - 사용자는 로그인을 하고 다시 전에 요청했던 정보의 캐싱내역을 꺼내서 사용할 수 있다.

### 1.3 인가 예외(AccessDeniedException) 처리
- `AccessDeniedHandler` 에서 예외 처리하도록 제공
- 참고로 rememberMe 사용자 또는 익명 사용자는 AuthenticationException의 하위 클래스 예외로 변경하여 인증 예외 처리 쪽으로 위임한다.

---

## 2. ExceptionTranslationFilter 흐름
- 인증
  - AuthorizationFiter에서 인증 예외가 발생하면 바깥으로 전파된다.
  - ExceptionTranslationFilter는 try-catch문에서 감싸서 예외를 처리한다.
  - **내부적으로 가지고 있는 RequestCache에 saveRequest 메서드를 호출에서 사용자 요청 정보를 캐싱한다.**
    - 인증에 실패한 사용자가 인증을 마치고 다시 원래 인증하려했던 페이지쪽으로 인증 후 리다이렉트 할 수 있도록 하기 위함이다.
    - 캐싱하지 않는 구현을 가진 RequestCache를 등록해두면 실제 캐싱은 이루어지지 않을 것이다.
    - 그림에서는 HttpSessionRequestCache 구현체가 사용된다. (이것은 기본 구현체이다.)
  - SecurityContext를 비운다.
  - AuthenticationEntryPoint의 commence() 를 호출하여 인증 예외 후속처리를 위임한다.
- 인가
  - `AuthorizationFilter`에서 인가 예외가 발생하면 바깥으로 전파된다.
  - `ExceptionTranslationFilter`는 try-catch문에서 감싸서 예외를 처리한다.
  - **이 때 익명 사용자(미인증 사용자)또는 `rememberMe` 사용자의 경우 인가 예외를 인증 예외로 바꿔서 인증 예외 처리 로직으로 위임한다.**
  - `AccessDeniedHandler`의 handle() 를 호출하여 인증 예외 후속처리를 위임한다.
- **주의할 점은, `ExceptionTranslationFilter` 앞에서 발생한 예외는 별도로 그 앞에서 개발자가 처리를 해야한다.**

---

### 6.3 인가 예외 처리
```java
private void handleAccessDeniedException(HttpServletRequest request, HttpServletResponse response,
			FilterChain chain, AccessDeniedException exception) throws ServletException, IOException {
		Authentication authentication = this.securityContextHolderStrategy.getContext().getAuthentication();
		boolean isAnonymous = this.authenticationTrustResolver.isAnonymous(authentication);
		if (isAnonymous || this.authenticationTrustResolver.isRememberMe(authentication)) {
			if (logger.isTraceEnabled()) {
				logger.trace(LogMessage.format("Sending %s to authentication entry point since access is denied",
						authentication), exception);
			}
			sendStartAuthentication(request, response, chain,
					new InsufficientAuthenticationException(
							this.messages.getMessage("ExceptionTranslationFilter.insufficientAuthentication",
									"Full authentication is required to access this resource")));
		}
		else {
			if (logger.isTraceEnabled()) {
				logger.trace(
						LogMessage.format("Sending %s to access denied handler since access is denied", authentication),
						exception);
			}
			this.accessDeniedHandler.handle(request, response, exception);
		}
}
```
- 인가 예외가 발생했다면
    - 시큐리티 컨텍스트에서 인증 객체를 읽어온다.
    - 익명 사용자이거나 리멤버미 인증 사용자일 경우 예외를 인증 예외로 변환하여 인증예외 처리 로직쪽으로 위임한다.
    - 그 외의 경우 인가 예외 후속처리를 **AccessDeniedHandler**에 위임한다.


# 3. 인증/인가 예외 후속처리 설정
```kotlin
exceptionHandling { 
	        authenticationEntryPoint = authenticationEntryPoint() // 인증 실패 시 처리
	        accessDeniedHandler = accessDeniedHandler() // 인가 실패 시 처리
}
```
- Kotlin DSL에서 설정하여, 예외 후속처리 로직을 담당하도록 하면 된다

---

# 4. 고민 : 예외처리 로직 분산
- ExceptionTranslationFilter 앞에서 발생한 예외는 그럼 어디서 처리해야하는걸까?
- DelegatingFilterProxy, FilterChainProxy 양쪽의 코드를 확인해보면, FilterChainProxy쪽에서 RequestRejectedException 이 아닌 예외들은 그대로 throw하고, requestRejectedException일 경우 어떤 핸들러(RequestRejectedHandler)를 통해 처리하는 것 같다.
- 별도로 예외를 커스텀처리 하지 않으면 WAS까지 예외가 전파될고 우리 뜻대로 처리할 수 없을 것이다.
- 따라서 필터 제일 앞단에, 예욍 처리 필터를 둬서 거기서 공통처리를 하도록 하는 것이 바람직할 것 같다.
- **따라서 Spring 기반 웹 애플리케이션 전반에서 처리해야할 예외는 다음과 같다.**
  - **DispatcherServlet 이후에서 발생한 예외들(컨트롤러, 인터셉터, … )**
    - `@ControllerAdvice`에서 처리할 수 있음
    - 키워드 : HandlerExceptionResolver
  - **ExceptionTranslationFilter 이후에 발생한 예외들**
    - AuthenticationEntryPoint : 인증 예외 후속 처리
      - AccessDenidedHandler: 인가 예외 후속 처리
  - **ExceptionTranslationFilter이전에 발생한 예외들**
    - 인증 필터에서 발생한 예외(예: Form 로그인 → 아이디, 패스워드 불일치) 들은, `AuthenticationFailureHandler`에게 위임하는 것이 기본적인 예외 처리 명세이다.
    - Jwt와 같이 필터체인 앞에 수동으로 추가한 필터에서 발생한 예외들은 이런 예외들은 앞단에 따로 필터를 설치해서 거기서 후속 처리를 하면 좋다.
    - 혹은 인증 필터의 경우 AuthenticationFailureHandler와 같은 핸들러에서 처리해도 좋다.
    - 의존성을 여기서 AuthenticationEntryPoint 쪽으로 둬도 무방할 것 같다.

---
