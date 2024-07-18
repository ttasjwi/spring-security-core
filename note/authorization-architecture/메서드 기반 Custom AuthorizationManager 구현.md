<nav>
    <a href="../../#authorization-architecture" target="_blank">[Spring Security Core]</a>
</nav>

# 메서드 기반 Custom AuthorizationManager 구현

- 목표 : 사용자 정의 AuthorizationManager 를 생성함으로 메서드 보안을 구현할 수 있다

---

## 1. 사용자 정의 AuthorizationManager 구현

### 1.1 메서드 보안 설정
```kotlin
@Configuration
@EnableMethodSecurity(prePostEnabled = false) // 시큐리티가 제공하는 기본 어드바이저 비활성화
class MethodSecurityConfig {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    fun preAuthorize(): Advisor {
        return AuthorizationManagerBeforeMethodInterceptor.preAuthorize(MyPreAuthorizeManager())
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    fun postAuthorize(): Advisor {
        return AuthorizationManagerAfterMethodInterceptor.postAuthorize(MyPostAuthorizationManager())
    }
}
```
- `@EnableMethodSecurity` 에서 `prePostEnabled = false` 옵션을 주면
  - 스프링 시큐리티가 기본으로 등록해준 PreAuthorize, PostAuthorize 에 대한 어드바이저가 등록되지 않는다.
- 우리가 커스텀하게 Advisor 를 등록하여, 프록시를 생성할 수 있다.
- 참고로, 여기서 사용되는 어드바이저의 생성자에 사용자 정의 AuthorizationManager 는 여러 개 추가할 수 있디.
  - 그럴 경우 체인 형태로 연결되어 각각 권한 검사를 하게 된다

### 1.2 필터체인 설정
```kotlin
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize(anyRequest, permitAll)
            }
            formLogin { }
        }
        return http.build()
    }
```
- permitAll -> 필터체인에서 인가처리를 하지 않고 모두 통과시킴

### 1.3 AuthorizationManager
```kotlin
class MyPreAuthorizeManager : AuthorizationManager<MethodInvocation> {

    override fun check(authenticationSupplier: Supplier<Authentication>, methodInvocation: MethodInvocation): AuthorizationDecision? {
        val authentication = authenticationSupplier.get()
        val authenticated = authentication !is AnonymousAuthenticationToken && authentication.isAuthenticated
        return AuthorizationDecision(authenticated)
    }

}
```
- MyPreAuthorizeManager 는 실제 메서드 호출 전에 호출되는 구조이다.
- 여기서는 인증 여부만 확인하고 인증을 통과시키도록 했다

```kotlin
class MyPostAuthorizationManager : AuthorizationManager<MethodInvocationResult> {

    override fun check(
        authenticationSupplier: Supplier<Authentication>,
        result: MethodInvocationResult
    ): AuthorizationDecision {

        val authentication = authenticationSupplier.get()
        val account = result.result as Account

        val isGranted = authentication !is AnonymousAuthenticationToken && account.owner == authentication.name
        return AuthorizationDecision(isGranted)
    }
}
```
- MyPostAuthorizationManager 는 실제 메서드 호출 후 호출되는 구조이다.
- 여기서는 인증된 사용자인지, Account 의 소유자와 인증자가 일치하는 지 확인하여 인가처리를 할 수 있게 했다.

### 1.4 Service
```kotlin
@Service
class DataService {

    @PreAuthorize("")
    fun getUser(): String {
        return "user"
    }

    @PostAuthorize("")
    fun getOwner(name: String): Account {
        return Account(name, false)
    }

    fun display(): String {
        return "display"
    }
}
```
- 서비스에서는 PreAuthorize, PostAuthorize 어노테이션의 value 값에 빈 문자열만 넣어줬다.
- 우리가 등록한 AuthorizationManager 에서는 내부 value 를 보지 않기 때문에 이렇게 해도 무방하다.

### 1.5 실행
- "/user" -> dataService.getUser -> PreAuthorize
  - 인증된 사용자인지 dataService.getUser 호출 시점에 확인하게 됨
- "/owner?name=xxx" -> dataService.getOwner -> PostAuthorize
  - dataService.getUser 반환 후 인증된 사용자인지, 인증된 사용자의 이름이 name 과 같은 지 확인하게 됨.
- "/display" -> dataService.diplay -> 보안설정 없음
  - 모두 통과됨

---

## 2. 인터셉터(어드바이저) 순서 지정
```java
public enum AuthorizationInterceptorsOrder {

	FIRST(Integer.MIN_VALUE), // 최소
	PRE_FILTER, // 100
	PRE_AUTHORIZE, // 200
	SECURED, // 300
	JSR250, // 400
	POST_AUTHORIZE, // 500
	POST_FILTER, // 600
	LAST(Integer.MAX_VALUE); // 최대
}
```
- 메서드 보안 어노테이션에 대응하는 AOP 메소드 인터셉터(어드바이저)들은 AOP 어드바이저 체인에서 특정 위치를 차지한다.
  - 예를 들어`@PreFilter` 메소드 인터셉터의 순서는 100, `@PreAuthorize`의 순서는 200 등으로 설정되어 있다
- 이것이 중요한 이유는 `@EnableTransactionManagement`와 같은 다른 AOP 기반 어노테이션들이
`Integer.MAX_VALUE` 로 순서가 설정되어 있는데 기본적으로 이들은 어드바이저 체인의 끝에 위치하고 있다는 점 때문이다.
- 만약 스프링 시큐리티보다 먼저 다른 어드바이스가 실행 되어야 할 경우, 예를 들어 `@Transactional` 과
`@PostAuthorize` 가 함께 어노테이션 된 메소드가 있을 때 `@PostAuthorize`가 실행될 때 트랜잭션이 여전히 열려있어
서 `AccessDeniedException` 이 발생하면 롤백이 일어나게 하고 싶을 수 있다
- 그래서 메소드 인가 어드바이스가 실행되기 전에 트랜잭션을 열기 위해서는 `@EnableTransactionManagement` 의 순
서를 설정해야 한다.

### 트랜잭션 어드바이저 순서 변경
- `@EnableTransactionManagement(order = 0)`
- 위의 order = 0 설정은 트랜잭션 관리가 `@PreFilter` 이전에 실행되도록 하며 `@Transactional` 어노테이션이 적용된 메소드가
스프링 시큐리티의 `@PostAuthorize` 와 같은 보안 어노테이션보다 먼저 실행되어 트랜잭션이 열린 상태에서 보안 검사가 이루어지도록
할 수 있다.
- 이러한 설정은 트랜잭션 관리와 보안 검사의 순서에 따른 의도하지 않은 사이드 이펙트를 방지할 수 있다
- 이렇게, AuthorizationInterceptorsOrder 를 참고하여 인터셉터 간 순서를 조절할 수 있다

---
