<nav>
    <a href="../../#authorization-process" target="_blank">[Spring Security Core]</a>
</nav>

# 메서드 기반 권한 부여 - @Secured, JSR-250 및 부가 기능

---

## 1. `@Secured`
```kotlin
    @GetMapping("/user")
    @Secured("ROLE_USER")
    fun user(): String {
        return "user"
    }
```
- `@Secured` 어노테이션을 메소드에 적용하면 지정된 권한(역할)을 가진 사용자만 해당 메소드를 호출할 수 있다.
- `@Secured` 어노테이션을 사용하려면 스프링 시큐리티 설정에서 `@EnableMethodSecurity(securedEnabled = true)` 설정을 활성화해야 한다
- 하지만 이 어노테이션보다 더 풍부한 형식을 지원하는 `@PreAuthorize` 을 사용하는 것을 권장.

---

## 2. JSR-250
- JSR-250 기능을 적용하면 `@RolesAllowed`, `@PermitAll` 및 `@DenyAll` 어노테이션 보안 기능이 활성화 된다. (`jakarta.annotation...`)
    ```kotlin
    
        @GetMapping("/admin")
        @RolesAllowed("ADMIN")
        fun admin(): String {
            return "admin"
        }
    
        @GetMapping("/permitAll")
        @PermitAll
        fun permitAll(): String {
            return "permitAll"
        }
    
        @GetMapping("/denyAll")
        @DenyAll
        fun denyAll(): String {
            return "denyAll"
        }
    ```
- JSR-250 어노테이션을 사용하려면 스프링 시큐리티 설정에서 `@EnableMethodSecurity(jsr250Enabled = true)` 설정을 활성화해야 한다

---

## 3. 그 외 부가 기능

### 3.1 커스텀 어노테이션
```kotlin
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@PreAuthorize("hasRole('ADMIN')")
annotation class IsAdmin
```
```kotlin
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@PostAuthorize("returnObject.owner == authentication.name")
annotation class OwnerShip
```
- 메서드 보안 기능은 애플리케이션의 특정 사용을 위해 편리성과 가독성을 높일 수 있도록 메타 어노테이션 기능을 제공한다.
- 커스텀 어노테이션에 메서드 보안 기능에서 제공하는 어노테이션을 합성할 수 있다.


### 3.2 특정 어노테이션만 활성화
```java
@EnableMethodSecurity(prePostEnabled = false)
class MethodSecurityConfig {
    
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    Advisor postAuthorize() {
        return AuthorizationManagerAfterMethodInterceptor.postAuthorize();
    }
}
```
- Method Security 의 사전 구성을 비활성화 한 다음, `@PostAuthorize` 를 활성화하면 특정 어노테이션에 대한 aop만 활성화시킬 수 있도록 할 수 있다.

### 3.3 커스텀 빈을 사용하여 표현식 구현하기
```kotlin
    @GetMapping("/delete")
    @PreAuthorize("@myAuthorizer.isUser(#operations)") // 빈,이름을,참조하고,접근,제어,로직을,수행한다
    fun delete(): String {
        return "delete"
    }
```
```kotlin
@Component("myAuthorizer")
internal class MyAuthorizer {
    fun isUser(operations: MethodSecurityExpressionOperations): Boolean {
        val decision = operations.hasAuthority("ROLE_USER") // 인증된,사용자가,ROLE_USER,권한을,가지고,있는지를,검사
        return decision
    }
}
```
- 사용자 정의 빈을 생성하고 새로운 표현식으로 사용할 메서드를 정의하고 권한 검사 로직을 구현하는 것이 가능하다.

### 3.4 클래스 레벨 권한 부여
```kotlin
@RestController
@PreAuthorize("hasAuthority('ROLE_ADMIN')") // 클래스 전체 적용
class RoleController {
    
    @GetMapping("/userRole")
    @PreAuthorize("hasAuthority('ROLE_USER')") // 더 구체적인 것이 우선 적용됨
    fun user(): String {
        return "user"
    }

    @GetMapping("/adminRole")
    fun admin(): String {
        return "admin"
    }
}
```
- 클래스 수준으로 권한 처리를 적용할 수 있는데 모든 메서드는 클래스 수준의 권한 처리 동작을 상속한다
- 메서드에 어노테이션을 선언한 메소드는 클래스 수준의 어노테이션을 덮어쓰게 된다 (구체적인 것을 우선)
- 인터페이스에도 동일한 규칙이 적용되지만 클래스가 두 개의 다른 인터페이스로부터 동일한 메서드의 어노테이션을 상속받는 경우에는 시작할 때 실패한다.
그래서 구체적인 메소드에 어노테이션을 추가함으로써 모호성을 해결할 수 있다.

---
