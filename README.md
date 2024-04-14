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
- <a href="/note/authentication-process/폼 인증 필터 - UsernamePasswordAuthenticationFilter.md" target="_blank">폼 인증 필터 - UsernamePasswordAuthenticationFilter</a>
- <a href="/note/authentication-process/Basic 인증 - httpBasic().md" target="_blank">Basic 인증 - httpBasic()</a>
- <a href="/note/authentication-process/Basic 인증 필터 - BasicAuthenticationFilter.md" target="_blank">Basic 인증 필터 - BasicAuthenticationFilter</a>
- <a href="/note/authentication-process/기억하기 인증 - rememberMe().md" target="_blank">기억하기 인증 - rememberMe()</a>
- <a href="/note/authentication-process/기억하기 인증 필터 - RememberMeAuthenticationFilter.md" target="_blank">기억하기 인증 필터 - RememberMeAuthenticationFilter</a>
- <a href="/note/authentication-process/익명 사용자 - anonymous().md" target="_blank">익명 사용자 - anonymous()</a>
- <a href="/note/authentication-process/로그아웃 - logout().md" target="_blank">로그아웃 - logout()</a>
- <a href="/note/authentication-process/요청 캐시 - RequestCache & SavedRequest.md" target="_blank">요청 캐시 - RequestCache & SavedRequest</a>

---

<h2 id="authentication-architecture">인증 아키텍쳐</h2>

- <a href="/note/authentication-architecture/인증 - Authentication.md" target="_blank">인증 - Authentication</a>
- <a href="/note/authentication-architecture/인증 컨텍스트 - SecurityContext, SecurityContextHolderStrategy, SecurityContextHolder.md" target="_blank">인증 컨텍스트 - SecurityContext, SecurityContextHolderStrategy, SecurityContextHolder</a>
- 인증 관리자 - AuthenticationManager
- 인증 제공자 - AuthenticationProvider
- 사용자 상세 서비스 - UserDetailsService
- 사용자 상세 - UserDetails

---

<h2 id="authentication-persistence">인증 상태 영속성</h2>
- SecurityContextRepository & SecurityContextHolderFilter
- 스프링 MVC 로그인 구현

---

<h2 id="sesion-management">세션 관리</h2>


---

<h2 id="exception-handling">예외 처리</h2>

---

<h2 id="exploit-protection">악용 보호</h2>

---

<h2 id="authorization-process">인가 프로세스</h2>

---

<h2 id="authorization-architecture">인가 아키텍쳐</h2>

---

<h2 id="event-handling">이벤트 처리</h2>

---

<h2 id="integration">통합하기</h2>

---

<h2 id="advanced-config">고급 설정</h2>

---

이하는 구버전

<h2 id="api-filter">스프링 시큐리티 기본 API 및 Filter 이해</h2>

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

---
