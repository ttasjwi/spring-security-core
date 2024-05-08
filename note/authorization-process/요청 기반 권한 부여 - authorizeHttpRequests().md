<nav>
    <a href="../../#authorization-process" target="_blank">[Spring Security Core]</a>
</nav>


# 요청 기반 권한 부여 - authorizeHttpRequests()

---

## 1. 인증과 인가
- Authentication : 인증 => 누구세요?
- Authorization : 인가, 권한 확인  => 누구인지는 알겠는데, 네가 이 자원에 접근할 권한이 있는지 확인해보겠다.
    - Spring Security 는 요청 기반 권한 부여(Request Based Authorization)와 메서드 기반 권한 부여(Method Based Authorization )를 통해 자원에 대한 심층적인 방어를 제공한다.
    - 요청 기반 권한 부여는 클라이언트의 요청 즉 HttpServletRequest 에 대한 권한 부여를 모델링 하는 것이며 이를 위해 HttpSecurity 인스턴스를 사용하여 권한 규칙을 선언 할 수 있다.

---

## 2. 권한 부여 방식

### 2.1 선언적 방식
- 요청 기반 권한 부여
    - 클라이언트의 요청 즉 HttpServletRequest 에 대한 권한 부여를 모델링 하는 것
    - 이를 위해 HttpSecurity 인스턴스를 사용하여 권한 규칙을 선언
- 메서드 단위 지정
    - 메서드 단위로 어노테이션을 지정하여 AOP 기반 권한 부여
    - 해당 메서드가 호출되기 전, 권한부여 수행

### 2.2 동적 방식 (DB 연동 프로그래밍)
- 동적으로 URL, 메서드에 적용할 수 있도록 설정
- 자원에 대한 특정 권한의 접근 권한을 런타임에 동적으로 변경 가능

---

## 3. 인가 API - 요청 기반 권한 설정

### 3.1 authorizeHttpRequests(...)
```java
@Bean
SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authorize -> authorize
        .anyRequest().authenticated() // 애플리케이션의 모든 엔드포인트가 최소한 인증된 보안 컨텍스트가 있어야 한다고 알림!!
    ); 
    
    return http.build();
}
```
- 사용자의 자원접근을 위한 요청 엔드포인트와 접근에 필요한 권한을 매핑시키기 위한 규칙을 설정하는 메서드
- 서블릿 기반 엔드포인트에 대한 접근을 이곳에 명시해야 한다.
- 이곳에서 요청에 대한 권한 규칙이 설정되면 내부적으로 AuthorizationFilter 가 요청에 대한 권한 검사 및 승인 작업을 수행한다


### 3.2 `requestMatchers()`
```kotlin
.requestMatchers("/admin/pay").hasRole("ADMIN")
```
- `requestMatchers` 메소드는 HTTP 요청의 URL 패턴, HTTP 메소드, 요청 파라미터 등을 기반으로 어떤 요청에 대해서는 특정 보안 설정을 적용하고 다른 요청에 대해서는 적용하
  지 않도록 세밀하게 제어할 수 있게 해 준다.
- 예를 들어 특정 API 경로에만 CSRF 보호를 적용하거나, 특정 경로에 대해 인증을 요구하지 않도록 설정할 수 있다.
- 이를 통해 애플리케이션의 보안 요구 사항에 맞춰서 유연한 보안 정책을 구성할 수 있다.
- 사용법: `requestMatchers(엔드포인트 패턴).hasXXX(권한규칙)`
- 파라미터
    - requestMatchers(String... urlPatterns) : 보호가 필요한 자원 경로를 한 개 이상 정의한다.
    - requestMatchers(RequestMatcher... requestMatchers) : 보호가 필요한 자원 경로를 한 개 이상 정의한다. AntPathRequestMatcher, MvcRequestMatcher 등의 구현체를 사용할 수 있다
    - requestMatchers(HttpMethod method, String... utlPatterns) : Http Method 와 보호가 필요한 자원 경로를 한 개 이상 정의한다


### 3.3 사용례 - kotlin
```kotlin
    @Bean
    fun securityFilterChain(http: HttpSecurity, introspector: HandlerMappingIntrospector): SecurityFilterChain {

        http {
            authorizeHttpRequests {
                authorize(OrRequestMatcher(AntPathRequestMatcher("/"), AntPathRequestMatcher("/login")), permitAll)
                authorize("/user", hasAuthority("ROLE_USER")) // "/user" 엔드포인트에 대해 "USER" 권한을 요구
                authorize("/mypage/**", hasRole("USER")) // "/mypage" 및 하위 디렉터리에 대해 "USER" 권한을 요구, Ant 패턴 사용
                authorize(HttpMethod.POST, "/**", hasAuthority("ROLE_WRITE")) // POST 메소드를 사용하는 모든 요청에 대해 "write" 권한을 요구
                authorize(AntPathRequestMatcher ("/manager/**"), hasAuthority("ROLE_MANAGER")) // "/manager" 및 하위 디렉터리에 대해 "MANAGER" 권한을 요구, AntPathRequestMatcher 사용.
                authorize(MvcRequestMatcher (introspector, "/admin/payment"), hasAuthority("ROLE_ADMIN")) // "/manager" 및 하위 디렉터리에 대해 "MANAGER" 권한을 요구. AntPathRequestMatcher 사용.
                authorize("/admin/**", hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")) // "/admin" 및 하위 디렉터리에 대해 "ADMIN" 또는 "MANAGER" 권한 중 하나를 요구
                authorize(RegexRequestMatcher ("/resource/[A-Za-z0-9]+", null) , hasAuthority("ROLE_MANAGER")) // 정규 표현식을 사용하여 "/resource/[A-Za-z0-9]+" 패턴에 "MANAGER" 권한을 요구
                authorize(anyRequest, authenticated) // 위에서 정의한 규칙 외의 모든 요청은 인증을 필요
            }
            formLogin {}
            csrf { disable() }
        }
        return http.build()
    }

```

---

## 4. 주의점
- 스프링 시큐리티는 클라이언트의 요청에 대하여 위에서 부터 아래로 나열된 순서대로 처리하며 요청에 대하여 첫 번째 일치만 적용되고 다음 순서로 넘어가지 않는다
- `/admin/**` 가 `/admin/db` 요청을 포함하므로 의도한 대로 권한 규칙이 올바르게 적용 되지 않을 수 있다. 그렇기 때문에 엔드 포인트 설정 시 좁은 범위의 경로를 먼저 정의하고 그
  것 보다 큰 범위의 경로를 다음 설정으로 정의 해야 한다

---

## 5. 인가 API - 권한 규칙 종류

| 메소드                        | 동작                                                  |
|----------------------------|-----------------------------------------------------|
| authenticated()            | 인증된 사용자의 접근을 허용                                     |
| fullyAuthenticated()       | 인증된 사용자의 접근을 허용, rememberMe 인증 제외                   |
| permitAll()                | 무조건 접근을 허용                                          |
| denyAll()                  | 무조건 접근을 허용하지 않음                                     |
| anonymous()                | 익명사용자의 접근을 허용 (일반 사용자의 허용은 이것만으로 정의되지 않아서 따로 설정해야함) |
| rememberMe()               | rememberMe 인증된 사용자의 접근을 허용                          |
| access(String)             | 주어진 SpEL 표현식의 평가 결과가 true이면 접근을 허용                  |
| hasRole(String)            | 사용자가 주어진 권한이 있다면 접근을 허용(ROLE_ 을 제외해야함)              |
| hasAuthority(String)       | 사용자가 주어진 권한이 있다면 접근을 허용                             |
| hasAnyRole(String...)      | 사용자가 주어진 권한 중 어떤 것이라도 있다면 접근을 허용(ROLE_ 을 제외해야함)     |
| hasAnyAuthority(String...) | 사용자가 주어진 권한 중 어떤 것이라도 있다면 접근을 허용                    |
| hasIpAddress(String)       | 주어진 IP로부터 요청이 왔다면 접근을 허용                            |


- 여기서 설정된 권한 규칙은 내부적으로 AuthorizationManager 클래스에 의해 재 구성되며 모든 요청은 여러 종류의 AuthorizationManager 에 설정된
  권한 규칙에 따라 승인 혹은 거부된다.

---
