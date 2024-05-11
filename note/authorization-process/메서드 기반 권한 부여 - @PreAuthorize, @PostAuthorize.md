<nav>
    <a href="../../#authorization-process" target="_blank">[Spring Security Core]</a>
</nav>


# 메서드 기반 권한 부여 - `@PreAuthorize`, `@PostAuthorize`

---

## 1. `@PreAuthorize`
```kotlin
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    fun admin() : String {
        return "admin"
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    fun user(): String {
        return "user"
    }

    @GetMapping("/isAuthenticated")
    @PreAuthorize("isAuthenticated()")
    fun isAuthenticated(): String {
        return "isAuthenticated"
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("#id == authentication.name")
    fun authentication(@PathVariable(name = "id") id: String): String {
        return id
    }
```
- `@PreAuthorize` 어노테이션은 메소드가 실행되기 전에 특정한 보안 조건이 충족되는지 확인하는 데 사용된다.
- 보통 서비스 또는 컨트롤러 레이어의 메소드에 적용되어 해당 메소드가 호출되기 전에 사용자의 인증 정보와 권한을 검사한다.

---


## 2. `@PostAuthorize`
```kotlin
    @GetMapping("/owner")
    @PostAuthorize("returnObject.owner == authentication.name")
    fun owner(name: String): AccountDto {
        return AccountDto(name, false)
    }

    @GetMapping("/isSecure")
    @PostAuthorize("hasAuthority('ROLE_ADMIN') and returnObject.isSecure")
    fun isSecure(name: String, secure: String): AccountDto {
        return AccountDto(name, "Y" == secure)
    }
```
- `@PostAuthorize` 어노테이션은 메소드가 실행된 후에 보안 검사를 수행하는 데 사용된다.
- `@PreAuthorize` 와는 달리, `@PostAuthorize`는 메소드 실행 후 결과에 대한 보안 조건을 검사하여
특정 조건을 만족하는 경우에만 사용자가 결과를 받을 수 있도록 한다.
- 반환 객체의 프로퍼티를 표현식에 사용할 수 있다.
- 실제 실행해보면 메서드는 실행되지만 반환에 실패한다.(인가 실패)

---
