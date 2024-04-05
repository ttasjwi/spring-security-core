# Spring-Security-Core
- 강의 링크 : <a href="https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0-%EC%99%84%EC%A0%84%EC%A0%95%EB%B3%B5" target="_blank">스프링 시큐리티 완전 정복</a>

---

<h2 id="kotlin">코틀린 관련</h2>

- <a href="/note/kotlin/코틀린 DSL.md" target="_blank">코틑린 DSL</a>

---

<h2 id="init">초기화 과정 이해</h2>

- <a href="/note/init/프로젝트 생성 & 의존성 추가.md" target="_blank">프로젝트 생성 & 의존성 추가</a>
- <a href="/note/init/SecurityBuilder & SecurityConfigurer.md" target="_blank">SecurityBuilder & SecurityConfigurer</a>
- <a href="/note/init/HttpSecurity & WebSecurity.md" target="_blank">HttpSecurity & WebSecurity</a>
- <a href="/note/init/Filter, DelegatingFilterProxy, FilterChainProxy.md" target="_blank">Filter, DelegatingFilterProxy, FilterChainProxy</a>
- <a href="/note/init/사용자 정의 보안 설정, 기본 사용자 설정.md" target="_blank">사용자 정의 보안 설정, 기본 사용자 설정</a>

---

<h2 id="authentication-process">인증 프로세스</h2>

- <a href="/note/authentication-process/폼 인증 - formLogin().md" target="_blank">폼 인증 - formLogin()</a>
- 폼 인증 필터 - UsernamePasswordAuthenticationFilter, AbstractAuthenticationProcessingFilter
- basic 인증 - httpBasic()
- basic 인증 필터 - BasicAuthenticationFilter
- 리멤버미 인증 - rememberMe()
- 리멤버미 인증 필터 - RememberMeAuthenticationFilter
- 익명인증 사용자 - anonymous()
- 로그아웃 - logout()
- 요청 캐시 - RequestCache / SavedRequest

---

이하는 구버전

<h2 id="api-filter">스프링 시큐리티 기본 API 및 Filter 이해</h2>

- <a href="/note/폼 로그인 인증.md" target="_blank">폼 로그인 인증</a>
- <a href="/note/로그아웃 처리, Logout Filter.md" target="_blank">로그아웃 처리, Logout Filter</a>
- <a href="/note/RememberMe 인증.md" target="_blank">RememberMe 인증</a>
- <a href="/note/동시세션 제어.md" target="_blank">동시세션 제어</a>
- <a href="/note/세션고정 보호.md" target="_blank">세션고정 보호</a>
- <a href="/note/세션생성 정책.md" target="_blank">세션생성 정책</a>
- <a href="/note/세션 제어 관련 필터 및 세션인증 전략(SessionAuthenticationStrategy).md" target="_blank">세션 제어 관련 필터 및 세션인증 전략(SessionAuthenticationStrategy)</a>
- <a href="/note/인가 API - 권한설정과 표현식.md" target="_blank">인가 API - 권한설정과 표현식</a>
- <a href="/note/예외 처리 및 요청 캐시 필터 - ExceptionTranslationFilter, RequestCacheAwareFilter.md" target="_blank">예외 처리 및 요청 캐시 필터 - ExceptionTranslationFilter, RequestCacheAwareFilter</a>
- <a href="/note/사이트 간 요청 위조 - CSRF, CsrfFilter.md" target="_blank">사이트 간 요청 위조 - CSRF, CsrfFilter</a>

---

<h2 id="architecture">스프링 시큐리티 주요 아키텍처 이해</h2>
- <a href="/note/architecture/다중 SecurityFilterChain.md" target="_blank">다중 SecurityFilterChain</a>
- <a href="/note/architecture/인증 개념 이해 - Authentication.md" target="_blank">인증 개념 이해 - Authentication</a>
- <a href="/note/architecture/SecurityContextHolder - 요청 사이클 동안 SecurityContext 홀딩.md" target="_blank">SecurityContextHolder - 요청 사이클 동안 SecurityContext 홀딩</a>

---
