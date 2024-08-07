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
- <a href="/note/authentication-architecture/인증 관리자 - AuthenticationManager.md" target="_blank">인증 관리자 - AuthenticationManager</a>
- <a href="/note/authentication-architecture/인증 제공자 - AuthenticationProvider.md" target="_blank">인증 제공자 - AuthenticationProvider</a>
- <a href="/note/authentication-architecture/사용자 상세 서비스 - UserDetailsService.md" target="_blank">사용자 상세 서비스 - UserDetailsService</a>
- <a href="/note/authentication-architecture/사용자 상세 - UserDetails.md" target="_blank">사용자 상세 - UserDetails</a>

---

<h2 id="authentication-persistence">인증 상태 영속성</h2>

- <a href="/note/authentication-persistence/SecurityContextRepository & SecurityContextHolderFilter.md" target="_blank">SecurityContextRepository & SecurityContextHolderFilter</a>
- <a href="/note/authentication-persistence/스프링 MVC 로그인 구현.md" target="_blank">스프링 MVC 로그인 구현</a>

---

<h2 id="sesion-management">세션 관리</h2>

- <a href="/note/session-management/동시 세션 제어.md" target="_blank">동시 세션 제어</a>
- <a href="/note/session-management/세션고정 보호.md" target="_blank">세션고정 보호</a>
- <a href="/note/session-management/세션생성 정책.md" target="_blank">세션생성 정책</a>
- <a href="/note/session-management/세션인증 전략(SessionAuthenticationStrategy) 및 세션관련 필터.md" target="_blank">세션인증 전략(SessionAuthenticationStrategy) 및 세션관련 필터</a>

---

<h2 id="exception-handling">예외 처리</h2>

- <a href="/note/exception-handling/스프링 시큐리티 예외 및 처리 API.md" target="_blank">스프링 시큐리티 예외 및 처리 API</a>
- <a href="/note/exception-handling/ExceptionTranslationFilter.md" target="_blank">ExceptionTranslationFilter</a>

---

<h2 id="exploit-protection">악용 보호</h2>

- <a href="/note/exploit-protection/CORS(Cross Origin Resource Sharing, 교차 출처 리소스 공유).md" target="_blank">CORS(Cross Origin Resource Sharing, 교차 출처 리소스 공유)</a>
- <a href="/note/exploit-protection/CSRF(Cross Site Request Forgery, 사이트 간 요청 위조).md" target="_blank">CSRF(Cross Site Request Forgery, 사이트 간 요청 위조)</a>
- <a href="/note/exploit-protection/CsrfToken, CsrfTokenRepository, CsrfTokenRequestHandler.md" target="_blank">CsrfToken, CsrfTokenRepository, CsrfTokenRequestHandler</a>
- <a href="/note/exploit-protection/CSRF 통합(클라이언트 + 백엔드).md" target="_blank">CSRF 통합(클라이언트 + 백엔드)</a>
- <a href="/note/exploit-protection/CsrfFilter.md" target="_blank">CsrfFilter</a>
- <a href="/note/exploit-protection/SameSite.md" target="_blank">SameSite</a>

---

<h2 id="authorization-process">인가 프로세스</h2>

- <a href="/note/authorization-process/요청 기반 권한 부여 - authorizeHttpRequests().md" target="_blank">요청 기반 권한 부여 - authorizeHttpRequests()</a>
- <a href="/note/authorization-process/표현식 및 커스텀 권한 구현.md" target="_blank">표현식 및 커스텀 권한 구현</a>
- <a href="/note/authorization-process/SecurityFilterChain 매칭 - securityMatcher().md" target="_blank">SecurityFilterChain 매칭 - securityMatcher()</a>
- <a href="/note/authorization-process/메서드 기반 권한 부여 - 활성화.md" target="_blank">메서드 기반 권한 부여 - 활성화</a>
- <a href="/note/authorization-process/메서드 기반 권한 부여 - @PreAuthorize, @PostAuthorize.md" target="_blank">메서드 기반 권한 부여 - `@PreAuthorize`, `@PostAuthorize`</a>
- <a href="/note/authorization-process/메서드 기반 권한 부여 - @PreFilter, @PostFilter.md" target="_blank">메서드 기반 권한 부여 - `@PreFilter`, `@PostFilter`</a>
- <a href="/note/authorization-process/메서드 기반 권한 부여 - @Secured, JSR-250 및 부가 기능.md" target="_blank">메서드 기반 권한 부여 - `@Secured`, JSR-250 및 부가 기능</a>
- <a href="/note/authorization-process/정적 자원 관리.md" target="_blank">정적 자원 관리</a>
- <a href="/note/authorization-process/계층적 권한 - RoleHierarchy.md" target="_blank">계층적 권한 - RoleHierarchy</a>

---

<h2 id="authorization-architecture">인가 아키텍쳐</h2>

- <a href="/note/authorization-architecture/인가 - GrantedAuthority.md" target="_blank">인가 - GrantedAuthority</a>
- <a href="/note/authorization-architecture/인가 관리자 - AuthorizationManager.md" target="_blank">인가 관리자 - AuthorizationManager</a>
- <a href="/note/authorization-architecture/요청 기반 인가 관리자.md" target="_blank">요청 기반 인가 관리자</a>
- <a href="/note/authorization-architecture/요청 기반 CustomAuthorizationManager 구현.md" target="_blank">요청 기반 CustomAuthorizationManager 구현</a>
- <a href="/note/authorization-architecture/RequestMatcherDelegatingAuthorizationManager 인가 설정 응용하기.md" target="_blank">RequestMatcherDelegatingAuthorizationManager 인가 설정 응용하기</a>
- <a href="/note/authorization-architecture/메서드 기반 인가 관리자.md" target="_blank">메서드 기반 인가 관리자</a>
- <a href="/note/authorization-architecture/메서드 기반 Custom AuthorizationManager 구현.md" target="_blank">메서드 기반 Custom AuthorizationManager 구현</a>
- <a href="/note/authorization-architecture/포인트 컷을 지정하여 메서드 보안 적용.md" target="_blank">포인트 컷을 지정하여 메서드 보안 적용</a>
- <a href="/note/authorization-architecture/AOP 메서드 보안 구현 - Advice, Pointcut, Advisor.md" target="_blank">AOP 메서드 보안 구현 - Advice, Pointcut, Advisor</a>

---

<h2 id="event-handling">이벤트 처리</h2>

- <a href="/note/event-handling/인증 이벤트 - Authentication Events.md" target="_blank">인증 이벤트 - Authentication Events</a>
- Authorization Events

---

<h2 id="integration">통합하기</h2>

- Servlet API 통합
- Spring MVC 통합

---

<h2 id="advanced-config">고급 설정</h2>

- 다중 보안 설정
- Custom DSL
- 이중화 설정(Redis, ...)

---

<h2 id="project">실전 프로젝트</h2>

- 회원 인증 시스템
- 회원 관리 시스템

---

이하는 구버전

<h2 id="architecture">스프링 시큐리티 주요 아키텍처 이해</h2>

- <a href="/note/architecture/다중 SecurityFilterChain.md" target="_blank">다중 SecurityFilterChain</a>

---
