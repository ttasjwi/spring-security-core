
# Spring-Security-Core

---

## 스프링 시큐리티 의존성 추가로 일어나는 일들
공식문서 : https://docs.spring.io/spring-security/reference/servlet/getting-started.html#servlet-hello-auto-configuration

```text
Using generated security password: 201aeffe-e657-4543-b787-0a35ea5f1800
This generated password is for development use only. Your security configuration must be updated before running your application in production.
```
- 서버가 기동되면 스프링 시큐리티의 초기화 작업 및 보안 설정이 이루어진다.
- 별도의 보안 설정, 구현을 하지 않아도 스프링 부트는 기본 필터를 적용하고, 모든 엔드포인트에 보안 기능이 적용된다.
    - 모든 요청은 인증이 되어야 자원에 접근이 가능하다.
    - 인증 방식으로는 Form 로그인 방식, HttpBasic 로그인 방식이 지원된다.
    - 기본 로그인 페이지가 제공된다.
    - 기본 계정이 하나 제공된다. (username: user / password : 랜덤 문자열)
- 웹 브라우저를 통해 루트 페이지로 접근하면, `/login` 으로 리다이렉트 된다. (기본 로그인 페이지)

```shell
$ curl -i localhost:8080

HTTP/1.1 401
Set-Cookie: JSESSIONID=0EF030C3ABB26D86ED4981C0E14F8E77; Path=/; HttpOnly
WWW-Authenticate: Basic realm="Realm"
X-Content-Type-Options: nosniff
X-XSS-Protection: 0
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
WWW-Authenticate: Basic realm="Realm"
Content-Length: 0
Date: Sun, 09 Jul 2023 09:22:24 GMT
```
- curl 명령어를 통해 API 요청을 하면, 401 UnAuthorized 응답이 온다.
- 해당 엔드포인트로 들어오는 요청을 다루는 핸들러를 개발하지 않았음에도 말이다.

---

## 커스텀 보안설정 활성화

### 코틀린 DSL
```kotlin
import org.springframework.security.config.annotation.web.invoke
```
- 이걸 import 시키면 코틀린 DSL 지원을 받아서 우아한 시큐리티 필터체인 작성이 가능해진다.
- IDE 지원을 못 받아서 우리가 수동으로 타이핑 해줘야 한다…
- 참고자료 : https://docs.spring.io/spring-security/reference/servlet/configuration/kotlin.html

### SecurityConfig
```kotlin
// @EnableWebSecurity
@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize(anyRequest, authenticated)
            }
            formLogin {  }
        }
        return http.build()
    }

}
```
- `@EnableWebSecurity` : 스프링 시큐리티를 활성화하고 웹 보안 설정을 구성하는데 사용됨
  - 사실, 스프링 부트를 사용한다면 이 어노테이션은 달 필요가 없다.
  - `SpringBootWebSecurityConfiguration` 클래스의 `WebSecurityEnablerConfiguration` 정적 내부 클래스를 통해 자동으로 활성화되기 때문이다.
  - 굳이 달지 않아도 되지만 이것이 스프링 시큐리티 설정 클래스라는 것을 다른 개발자들에게 어노테이션을 통해 명시적으로 명시하고 싶다면 달아주면 된다.
- `@Configuration` : 설정 클래스

---

## 디폴트 사용자 이름, 패스워드 수정
```yaml
# application.yml
spring:
  security:
    user:
      name: user
      password: 1111
```
application.yml 을 통해 디폴트 사용자 이름, 패스워드를 수정 가능하다

---
