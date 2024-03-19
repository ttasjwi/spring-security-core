<nav>
    <a href="../#api-filter" target="_blank">[Spring Security Core]</a>
</nav>

# 인가 API - 권한설정과 표현식

---

## 인증과 인가

- Authentication : 인증
    - 누구세요?
- Authorization : 인가, 권한 확인
    - 누구인지는 알겠는데, 네가 이 자원에 접근할 권한이 있는지 확인해보겠다.

---

## 권한 설정 방식
### 선언적 방식
- 설정 클래스에서 URL 단위 지정
- 메서드 단위 지정 : 메서드에 AOP

### 동적 방식 (DB 연동 프로그래밍)
- 동적으로 URL, 메서드에 적용할 수 있도록 설정
- 자원에 대한 특정 권한의 접근 권한을 런타임에 동적으로 변경 가능

---

## 인가 API - 설정 클래스
- 주의점 : 설정 시 구체적인 것이 먼저 오고, 그보다 큰 범위의 경로가 뒤에 오도록 해야한다.
### 3.1 메서드 체이닝 방식
```kotlin
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .formLogin {}
            .authorizeHttpRequests {
                it.requestMatchers("/user").hasRole("USER")
                    .requestMatchers("/admin/pay").hasRole("ADMIN")
                    .requestMatchers("/admin/**")
                    .access(WebExpressionAuthorizationManager("hasRole('ADMIN') or hasRole('SYS')"))
                    .anyRequest().authenticated()
            }
            .build()
```

### 3.2 코틀린 - DSL 방식
```kotlin
@Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            formLogin {  }
            authorizeRequests {
                authorize("/user", hasRole("USER"))
                authorize("/admin/pay", hasRole("ADMIN"))
                authorize("/admin/**", access = "hasRole('ADMIN') or hasRole('SYS')")
                authorize(anyRequest, authenticated)
            }
        }
        return http.build()
    }
```

---

## 인가 API - 표현식

| 메소드                        | 동작                                                  |
|----------------------------|-----------------------------------------------------|
| authenticated()            | 인증된 사용자의 접근을 허용                                     |
| fullyAuthenticated()       | 인증된 사용자의 접근을 허용, rememberMe 인증 제외                   |
| permitAll()                | 무조건 접근을 허용                                          |
| denyAll()                  | 무조건 접근을 허용하지 않음                                     |
| anonymous()                | 익명사용자의 접근을 허용 (일반 사용자의 허용은 이것만으로 정의되지 않아서 따로 설정해야함) |
| rememberMe()               | rememberMe 인증된 사용자의 접근을 허용                          |
| access(String)             | 주어진 SpEL 표현식의 평가 결과가 true이면 접근을 허용                  |
| hasRole(String)            | 사용자가 주어진 역할이 있다면 접근을 허용                             |
| hasAuthority(String)       | 사용자가 주어진 권한이 있다면                                    |
| hasAnyRole(String...)      | 사용자가 주어진 권한이 있다면 접근을 허용                             |
| hasAnyAuthority(String...) | 사용자가 주어진 권한 중 어떤 것이라도 있다면 접근을 허용                    |
| hasIpAddress(String)       | 주어진 IP로부터 요청이 왔다면 접근을 허용                            |


---

## 실습

### 컨트롤러 설정
```kotlin
@RestController
class SecurityController {

    @GetMapping("/")
    fun index() = "home"

    @GetMapping("/user")
    fun user() = "user"

    @GetMapping("/sys")
    fun sys() = "sys"

    @GetMapping("/admin/pay")
    fun adminPay() = "adminPay"

    @GetMapping("/admin/**")
    fun admin() = "admin"
}
```
- - `/` 는 인증된 모든 사용자가 접근 가능
- `/user` 는 USER 역할 사용자
- `/sys` 는 SYS 역할 사용자
- `/admin/**` 는 SYS 역할 또는 ADMIN 역할
- `/admin/pay` 는 ADMIN 역할


### 설정 클래스
```kotlin
@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            formLogin {  }
            authorizeRequests {
                authorize("/user", hasRole("USER"))
                authorize("/sys", hasRole("SYS"))
                authorize("/admin/pay", hasRole("ADMIN"))
                authorize("/admin/**", access = "hasRole('ADMIN') or hasRole('SYS')")
                authorize(anyRequest, authenticated)
            }
        }
        return http.build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val user = User.withDefaultPasswordEncoder().username("user").password("1111").roles("USER").build()
        val sys = User.withDefaultPasswordEncoder().username("sys").password("1111").roles("SYS", "USER").build()
        val admin = User.withDefaultPasswordEncoder().username("admin").password("1111").roles("ADMIN", "SYS", "USER").build()

        return InMemoryUserDetailsManager(user, sys, admin)
    }
}
```
- DSL로 권한 설정을 간편하게 할 수 있다.
- 이때, 구체적인 것을 위에 둬야함에 주목하자.
- 사용자를 수동등록하기 위해 UserDetailsService를 빈으로 등록한다. 여기서 우리는 InMemoryUserDetailsManager를 구현체로 등록하였다.

### 실행
- 위 상태로 실행할 경우
  - user는 `/` 또는 `/user`
  - sys 는 `/` 또는 `/user` 또는 `/sys` 또는 `/admin/**` (단 `/admin/pay` 제외)
  - admin 은 `/` 또는 `/user` `/sys` 또는 `/admin/**` (`/admin/pay` 포함)
  - 에 접근 가능하다.
- 그런데 /admin/pay 줄과 /admin/** 줄의 순서를 바꾸면 sys 사용자가 admin/pay에 접근할 수 있게 된다. 위에 적용된 설정이 먼저 적용되기 떄문이다. **구체적인 것을 위에 둬야함을 잊지 말자.**

---

## 한계
- 여기서 우리는 USER, SYS, ADMIN 의 등급 차이를 명시적으로 부여하지 않고 더 권한이 많은 등급에  모든 역할을 부여했다. ADMIN 하나만 부여하면 그 아래 역할의 권한이 모두 부여되게 할 수는 없는걸까? 그것은 뒤에서 다룰 예정이다.
- 사실 이렇게 정적으로 각각의 엔드포인트에 대한 접근권한을 설정파일에 모두 걸어두는 것은 유연하지 못 하다. 실시간 요구사항은 자주자주 변하는데 런타임에 자유자재로 특정 엔드포인트에 대한 접근권한을 수정할 수 있어야 한다. 요구사항이 바뀔 때마다 엔드포인트 접근 설정을 바꾸고 다시 빌드를 하는 것은 유연하지 못 하다.

---

