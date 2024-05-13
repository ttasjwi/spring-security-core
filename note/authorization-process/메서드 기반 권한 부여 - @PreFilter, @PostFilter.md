<nav>
    <a href="../../#authorization-process" target="_blank">[Spring Security Core]</a>
</nav>

# 메서드 기반 권한 부여 - @PreFilter, @PostFilter

---

## 1. `@PreFilter`
```kotlin
    @PreFilter("filterObject.owner == authentication.name")
    fun writeList(accounts: List<Account>): List<Account> {
        return accounts
    }

    @PreFilter("filterObject.value.owner == authentication.name")
    fun writeMap( accounts: Map<String, Account>): Map<String, Account> {
        return accounts
    }
```
- `@PreFilter` 어노테이션은 메소드가 실행되기 전에 메소드에 전달된 컬렉션 타입의 파라미터에 대한 필터링을 수행하는데 사용된다.
- `@PreFilter` 어노테이션은 주로 사용자가 보내온 컬렉션(배열, 리스트, 맵, 스트림) 내의 객체들을 특정 기준에 따라 필터링하고 그 중 보안 조건을 만족하는 객체들에 대해서만 메소드가
처리하도록 할 때 사용된다.

---

## 2. `@PostFilter`
```kotlin
    @PostFilter("filterObject.owner == authentication.name")
    fun readList(): List<Account> {
        return repository.values.toList()
    }

    @PostFilter("filterObject.value.owner == authentication.name")
    fun readMap(): Map<String, Account> {
        return repository.toMap()
    }
```
- `@PostFilter` 어노테이션은 메소드가 반환하는 컬렉션 타입의 결과에 대해 필터링을 수행하는 데 사용된다
- `@PostFilter` 어노테이션은 메소드가 컬렉션을 반환할 때 반환되는 각 객체가 특정 보안 조건을 충족하는지 확인하고 조건을 만족하지 않는 객체들을 결과에서 제거한다

---

## 3. 실습

### 3.1 설정
```kotlin
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize(anyRequest, authenticated)
            }
            formLogin {  }
            csrf { disable() }
        }
        return http.build()
    }
```
- CSRF 기능을 비활성화했다.

### 3.2 `/method` 페이지
- 타임리프 의존성 추가
    ```kotlin
    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    }
    ```
- /method view 이름을 반환하는 컨트롤러 추가
    ```kotlin
    @Controller
    class ViewController {
    
        @GetMapping("/method")
        fun method(): String {
            return "method"
        }
    }
    ```
- method 페이지(`/resources/templates/method.html`)
    ```html
    <!DOCTYPE html>
    <html xmlns:th="http://www.thymeleaf.org">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Button Click Example</title>
        <script>
            function sendRequest(url) {
    
                const dataItems = [
                    {"owner": "user", "isSecure": false},
                    {"owner": "admin", "isSecure": true},
                    {"owner": "db", "isSecure": false}
                ];
    
                fetch(url, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(dataItems),
                })
                    .then(response => {
                        if (response.status < 400) {
                            return response.json();
                        } else {
                            throw new Error('Network response was not ok.');
                        }
                    })
                    .then(data => console.log(data))
                    .catch(error => console.error('There was a problem with your fetch operation:', error));
            }
        </script>
    </head>
    <body>
    
    <p>
        <button onclick="sendRequest('/writeList')">Pre Account List</button>
    </p>
    <p>
        <button onclick="sendRequest('/writeMap')">Pre Account Map</button>
    </p>
    <p><a href="/readList">Post Account List</a></p>
    <p><a href="/readMap">Post Account Map</a></p>
    
    </body>
    </html>
    ```

### 3.3 Account
```kotlin
package com.security.domain

class Account (
    val owner: String,
    val isSecure: Boolean,
)
```
- 편의상 요청/응답 모델을 도메인 객체로 지정했다.

### 3.4 SecurityController
```kotlin
@RestController
class SecurityController(
    private val accountService: AccountService
) {

    @GetMapping("/")
    fun index(): String {
        return "index"
    }

    @PostMapping("/writeList")
    fun writeList(@RequestBody accounts: List<Account>): List<Account> {
        return accountService.writeList(accounts)
    }

    @PostMapping("/writeMap")
    fun writeMap(@RequestBody accounts: List<Account>): Map<String, Account> {
        val dataMap = accounts.associateBy { it.owner }
        return accountService.writeMap(dataMap)
    }

    @GetMapping("/readList")
    fun readList(): List<Account> {
        return accountService.readList()
    }

    @GetMapping("/readMap")
    fun readMap(): Map<String, Account> {
        return accountService.readMap()
    }
}
```
- 요청을 통해 전달된 객체들을 accountService에 넘기고 결과를 반환받아 응답한다.
- 참고: writeMap 메서드에서 associateBy 는 리스트를 map으로 변환하는 코틀린 메서드이다.

### 3.5 AccountService
```kotlin
@Service
class AccountService {

    private val repository = mapOf(
        "user" to Account("user", false),
        "db" to Account("db", false),
        "admin" to Account("admin", true)
    )

    @PreFilter("filterObject.owner == authentication.name")
    fun writeList(accounts: List<Account>): List<Account> {
        return accounts
    }

    @PreFilter("filterObject.value.owner == authentication.name")
    fun writeMap( accounts: Map<String, Account>): Map<String, Account> {
        return accounts
    }

    @PostFilter("filterObject.owner == authentication.name")
    fun readList(): List<Account> {
        return repository.values.toList()
    }

    @PostFilter("filterObject.value.owner == authentication.name")
    fun readMap(): Map<String, Account> {
        return repository.toMap()
    }
}
```
- write 메서드들은, 입력으로 들어온 인자들을 @PreFilter을 통해 조건에 맞는 것만 인자로 전달하게 한다.
- read 메서드들은 반환값을 @PostFilter 을 통해 조건에 맞는 것만 반환하게 한다.

### 3.6 실행
- 실제 실행하여 `/method`에 접속
- 버튼 및 url들을 실행해보면 입력/출력이 accountService에 적용된 aop로 인해 필터링된 것을 확인할 수 있다.

---
