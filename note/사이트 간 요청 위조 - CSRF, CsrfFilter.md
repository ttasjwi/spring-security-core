<nav>
    <a href="../#api-filter" target="_blank">[Spring Security Core]</a>
</nav>

# 사이트 간 요청 위조 - CSRF, CsrfFilter

## 1. CSRF 공격이란?
![csrf-1](/imgs/csrf-1.png)

**Cross-site Request Forgery**
1. 사용자가 로그인 후 쿠키를 발급받는다.
2. 공격자는 공격 스크립트 또는 링크가 섞인 공격 게시글을 올리고, 사용자는 그걸 클릭한다.
   - 예) “Adobe Photoshop 2023 다운로드”, “한글 2022.torrent”, …
3. 해당 HTML 파일에는 공격 링크 또는 스크립트 실행 코드가 작성되어 있다. 사용자의 승인, 인지 없이 공격용 URL을 받아오거나 스크립트가 실행된다.

## 2. CSRF 공격 방어 아이디어
- 생각
    - 웹 페이지에서 데이터를 변경하는 작업이 포함된 페이지는 결국 먼저 사용자가 HTTP GET으로 웹 페이지를 요청해야 한다.
    - 변경 기능이 포함된 페이지를 GET 요청으로 전송할 때 CSRF 인증을 위한 값을 함께 전달하여 보내고 변경 요청(POST, PUT, PATCH, DELETE) 때 함께 보내도록 하는 것은 어떨까?
- **매 요청마다, CSRF 토큰을 Http 파라미터로 요구하게 한다.** CSRF 헤더명, 헤더값은 hidden 필드로 섞어 전달하도록 하여, 공격자가 예측할 수 없게 한다. 애플리케이션은 이후 헤더에 이 고유한 토큰이 포함된 요청에 대해서만 변경 작업(POST, PUT, PATCH, DELETE 등)을 수행하도록 한다.
    - 헤더에 담아 보내거나, 요청 파라미터에 전달하는 방식으로 처리할 수 있다.
- 요청시 전달되는 토큰 값과 서버에 저장된 실제 값을 비교한 후, 만약 일치하지 않으면 요청은 실패하도록 한다.


## 3. CSRF 필터
![csrf-2](/imgs/csrf-2.png)

CsrfFilter는 내부적으로 크게 4개의 컴포넌트를 의존하고 있다.

- CsrfTokenRepository : 토큰 생성, 저장, 조회, 지연토큰 가져오기
- CsrfTokenRequestHandler : 요청을 조작
- AccessDeniedHandler : Csrf 토큰 검증에서 실패했을 때 후속 예외 처리
- RequestMatcher : 요청을 매칭하여 true, false를 반환하는 전략

### 3.1 CsrfTokenRepository
```java
public interface CsrfTokenRepository {

	// 요청정보를 기반으로 csrf 토큰 생성
	CsrfToken generateToken(HttpServletRequest request);

	// csrf 토큰 저장(요청, 응답 정보를 사용)
	// csrf 토큰이 null 이면 삭제하라는 요청과 같음
	void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response);

	// 요청 정보를 기반으로 csrf 토큰 가져오기
	CsrfToken loadToken(HttpServletRequest request);

	// 애플리케이션의 필요할때 까지 로딩을 미루는 CSRF토큰(요청, 응답 정보를 기반으로)을 얻어옴
	default DeferredCsrfToken loadDeferredToken(HttpServletRequest request, HttpServletResponse response) {
		return new RepositoryDeferredCsrfToken(this, request, response);
	}

}
```
- 요청을 기반으로 Csrf 토큰을 생성, 저장, 조회해오는 역할
- 구현체
    - HttpSessionCsrfTokenRepository
        - 세션 기반(기본값)
        - 토큰 정보를 X-CSRF-TOKEN 헤더 또는 요청 파라미터의 _csrf 파라미터로부터 읽어옴
    - CookieCsrfTokenRepository : 쿠키에 CsrfToken 저장
- **loadDeferredToken** : 지연 토큰을 로딩하는 메서드
    - Spring Security 5.8 이후 이 방식을 통해 지연토큰(DeferredToken)을 가져온다.
    - 여기서 반환되는 지연토큰의 구현체는 `RepositoryDeferredCsrfToken` 이다.
        - 토큰은 자신이 저장된 리포지토리를 알고 있다.
    - get 을 호출하는 순간, init 메서드가 호출된다. 초기화되어 있으면 CsrfToken을 반환하고, 초기화되어 있지 않으면 초기화 작업을 실행한다.
        - 리포지토리에 자신이 저장되어 있으면 load 해온다.
        - 리포지토리에 자신이 저장되어 있지 않으면 generateToken 해오고 저장한다.
        - 토큰을 반환한다.

### 3.2 CsrfTokenRequestHandler
```java
@FunctionalInterface
public interface CsrfTokenRequestHandler extends CsrfTokenRequestResolver {
		
		// 요청을 handle
		void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken);
		
		@Override
		default String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
				Assert.notNull(request, "request cannot be null");
				Assert.notNull(csrfToken, "csrfToken cannot be null");
				String actualToken = request.getHeader(csrfToken.getHeaderName());
				if (actualToken == null) {
						actualToken = request.getParameter(csrfToken.getParameterName());
				}
				return actualToken;
		}

}
```
- CsrfToken을 요청에 기록하거나, 요청으로부터 토큰을 파싱하는 역할
  - handle : 요청, 응답, Csrf 토큰을 기반으로 요청을 조작
  - resolveCsrfTokenValue : 사용자 요청의 헤더 또는 파라미터에서 토큰을 읽어와서 파싱

### 3.3 RequestMatcher
```java
public interface RequestMatcher {
	// 생략

	boolean matches(HttpServletRequest request);

}
```
```java
@Override
public boolean matches(HttpServletRequest request) {
	return !this.allowedMethods.contains(request.getMethod());
}
```
- Csrf 인증이 필요한 지 확인하는 역할을 수행한다.(변수명 : requireCsrfProtectionMatcher)
- 실제 런타입에 주입된 구현체는 내부적으로 “TRACE”, “HEAD”, “GET”, “OPTIONS” 를 해시셋에 갖고 있는데, 요청의 Http 메서드가 이들에 속하지 않으면 true를 반환한다.
- 즉, 부작용(side effect)가 있는 메서드이면 이 Matcher는 true를 반환한다.

### 3.4 AccessDeniedHandler
```java
public interface AccessDeniedHandler {

	void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
			throws IOException, ServletException;

}
```
- 인가 예외의 후속처리를 담당하는 역할을 수행한다.
- 보통 개발자가 수동으로 구현해서 스프링 시큐리티에 등록하면 된다.
- 주 사용 목적은 ExceptionTranslationFilter에서 인가예외 후속처리에 사용된다.

---

## 4. CSRF 필터 흐름
![csrf-3](/imgs/csrf-3.png)

```java
@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
					throws ServletException, IOException {
		// 요청, 응답을 기반으로 토큰 리포지토리에서 지연토큰을 가져옴(토큰이 없더라도 초기화가 예정됨)
		DeferredCsrfToken deferredCsrfToken = this.tokenRepository.loadDeferredToken(request, response);
		
		// 요청에 지연 토큰을 저장함
		request.setAttribute(DeferredCsrfToken.class.getName(), deferredCsrfToken);
		
		// 요청 객체에 지연 토큰을 저장, 갱신하여 저장
		this.requestHandler.handle(request, response, deferredCsrfToken::get);
		
		// CSRF 인증이 필요없으면 스킵
		if (!this.requireCsrfProtectionMatcher.matches(request)) {
			if (this.logger.isTraceEnabled()) {
				this.logger.trace("Did not protect against CSRF since request did not match "
						+ this.requireCsrfProtectionMatcher);
			}
			filterChain.doFilter(request, response);
			return;
		}

		// 지연 토큰 로딩
		CsrfToken csrfToken = deferredCsrfToken.get();

		// 사용자가 보낸 요청의 토큰(actualToken)을 파싱
		String actualToken = this.requestHandler.resolveCsrfTokenValue(request, csrfToken);
		
		// 지연토큰과 요청 토큰을 비교하여 같은지 검증
		if (!equalsConstantTime(csrfToken.getToken(), actualToken)) {
			
			// 같지 않으면 AccessDenidedHandler에 예외를 전달
			boolean missingToken = deferredCsrfToken.isGenerated();
			this.logger.debug(
					LogMessage.of(() -> "Invalid CSRF token found for " + UrlUtils.buildFullRequestUrl(request)));
			AccessDeniedException exception = (!missingToken) ? new InvalidCsrfTokenException(csrfToken, actualToken)
					: new MissingCsrfTokenException(actualToken);
			this.accessDeniedHandler.handle(request, response, exception);
			return;
		}
		filterChain.doFilter(request, response);
}
```
- CsrfTokenRepository에서 지연Csrf토큰을 얻어온다.
- 요청에 지연 토큰을 저장한다.
- CsrfTokenRequestHandler를 통해 지연토큰을 갱신, 저장한다.
- CSRF 인증이 필요하면 현재 필터 로직을 마치고 doFilter를 호출한다.
- 지연 토큰을 로딩한다. (이 시점에 토큰 인스턴스가 실제 생성된다.)
- 사용자가 보낸 요청 토큰을 파싱한다.
- 둘을 비교하여
    - 같지 않으면 예외 객체를 생성하고 AccessDeniedHandler에 처리를 위임한다.
    - 같으면 다음 필터로 doFilter를 호출하여 넘어간다.

---

## 5. CSRF 보호 설정
- 클라이언트 설정
    ```html
    <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}" />
    ```
    - Hidden 필드에 csrf 토큰을 함께 섞어서 보낼 수 있다.
    - 타임리프와 같은 뷰템플릿의 경우 경우 _csrf 변수를 통해 요청 파라미터에서 csrf 토큰의 파라미터명, 토큰명을 꺼내 쓸 수 있다.(form 태그를 사용할 때도 그냥 자동으로 삽입해주기도 한다.) 이를 통해 페이지에 csrf 토큰을 함께 전달할 수 있다.
        - 다만 jsp 같은 경우 이런 지원을 지원을 받을 수 없고 수동으로 작성해줘야 한다.
- 스프링 시큐리티 설정
    ```kotlin
    csrf {
    
    }
    ```
    - `http.csrf()` : 기본적으로 활성화되어 있음
    - `http.csrf().disabled()` : **비활성화 → 필터가 제거됨**
    - 커스텀 설정(CsrfDsl.kt 참고)
        - `csrfTokenRepository` : 토큰 리포지토리 구현체 설정(기본 : HttpSessionTokenRepository)
        - `requireCsrfProtectionMatcher` : 토큰 인증이 필요한 요청을 필터링하기 위한 RequestMatcher
        - `csrfTokenRequestHandler` : csrfTokenHandler 설정

---

## 6. 실습

### 6.1 설정
```kotlin
@EnableWebSecurity
@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize(anyRequest, permitAll)
            }
            formLogin {

            }
        }
        return http.build()
    }

}
```

- 모든 요청에 대해 허가하도록 했다.
- Csrf 기본 설정을 사용하도록 한다. 물론 명시적으로 Csrf 필터를 사용한다고 선언하는 관점에서 csrf dsl을 명시해도 된다.

### 6.2 컨트롤러 설정
```kotlin
@RestController
class SecurityController {

    @GetMapping("/")
    fun index(request: HttpServletRequest): String {
        val csrfToken = request.getAttribute("_csrf") as CsrfToken
        println("CSRF HeaderName = ${csrfToken.headerName}")
        println("CSRF ParamName = ${csrfToken.parameterName}")
        println("CSRF token = ${csrfToken.token}")
        return "home"
    }

		@PostMapping("/")
    fun poseIndex() = "postHome"
}
```

- `/` Get 요청 시 요청에 저장된 csrfToken 을 읽어와 헤더명, 파라미터명, 토큰을 추출하여 출력한 뒤 “home” 을 반환한다.
- `/` POST 요청 시 postHome 을 출력한다.
    - **POST는 CSRF 토큰 검사의 대상이 된다.**

### 6.3 "/"로 GET 요청
![csrf-4](/imgs/csrf-4.png)

RequireCsrfProtectionMatcher에서 체크하지 않는 메서드이므로 바로 doFilter가 호출되고 Csrf 토큰 검증을 받지 않는다.


```shell
CSRF HeaderName = X-CSRF-TOKEN
CSRF ParamName = _csrf
CSRF token = EBvEz-rUaD490A--xPO5DcX6FF-4Lo-T6--K_G43ZgTuON98JS_w-4_gCg0Q6WqH9t6NP6aYOWaKTLi-idroxV0FVzaIXOwe
```
- 앞선 과정에서 Csrf 토큰이 Request에 작성되므로 Csrf 토큰을 요청에서 꺼낼 수 있다.
- 콘솔에 Csrf 토큰 관련 파라미터들이 출력된다.

### 6.4 "/"로 POST 요청
![csrf-5](/imgs/csrf-5.png)

```java
CsrfToken csrfToken = deferredCsrfToken.get();
String actualToken = this.requestHandler.resolveCsrfTokenValue(request, csrfToken);
if (!equalsConstantTime(csrfToken.getToken(), actualToken)) {
    boolean missingToken = deferredCsrfToken.isGenerated();
    this.logger
        .debug(LogMessage.of(() -> "Invalid CSRF token found for " + UrlUtils.buildFullRequestUrl(request)));
    AccessDeniedException exception = (!missingToken) ? new InvalidCsrfTokenException(csrfToken, actualToken)
            : new MissingCsrfTokenException(actualToken);
    this.accessDeniedHandler.handle(request, response, exception);
    return;
}
filterChain.doFilter(request, response);
```
- POST 요청은 CSRF 필터의 검증 대상이다.
- 지연 토큰이 로딩된다.
- resolveCsrfTokenValue 메서드를 통해 요청에서 토큰이 추출된다.
- 요청 토큰와 실제 토큰을 비교하여 올바른지 검증을 수행한다.
- 일치하므로 필터를 통과한다.


![csrf-6](/imgs/csrf-6.png)

200 응답이 오면서 postHome 문자열이 전달된다.

---
