<nav>
    <a href="../../#authorization-process" target="_blank">[Spring Security Core]</a>
</nav>

# 계층적 권한 - RoleHierarchy

---

## 1. 계층적 권한이란?

### 1.1 개요
- 기본적으로 스프링 시큐리티에서 권한과 역할은 계층적이거나 상하 관계로 구분하지 않는다. 그래서 인증 주체가 다양한 역할과 권한을 부여 받아야 한다
- RoleHierarchy 는 역할 간의 계층 구조를 정의하고 관리하는 데 사용되며, 보다 간편하게 역할 간의 계층 구조를 설정하고 이를 기반으로 사용자에 대한 액세스
규칙을 정의할 수 있다.
- 예시
  - ROLE_A 를 가진 모든 사용자는 ROLE_B, ROLE_C 및 ROLE_D 도 가지게 된다
  - ROLE_B 를 가진 모든 사용자는 ROLE_C 및 ROLE_D도 가지게 된다
  - ROLE_C 를 가진 모든 사용자는 ROLE_D도 가지게 된다
- 이와 같이 계층적 역할을 사용하면 액세스 규칙이 크게 줄어들 뿐만 아니라 더 간결하고 우아한 형태로 규칙을 표현할 수 있다.

### 1.2 RoleHierarchy
```java
public interface RoleHierarchy {
	Collection<? extends GrantedAuthority> getReachableGrantedAuthorities(
			Collection<? extends GrantedAuthority> authorities);
}
```
- 어떤 리소스에 대해, 직접 지정되어 있는 권한 목록이 주어지면 이를 토대로 도달 가능한 모든 권한 컬렉션을 반환하는 역할을 한다.
- 도달 가능한 권한은 직접 할당된 권한에 더해 역할 계층에서 이들로부터 도달가능한 모든 권한을 의미한다
- 예시
  - 직접 지정한 권한 : Role_A
  - 도달 가능한 권한 : ROLE_A, ROLE_B, ROLE_C
  - 예를 들어 Role_A가 인자로 전달됐을 때 ROLE_A, ROLE_B, ROLE_C 컬렉션을 반환받게 된다.

### 1.3 NullRoleHierarchy
```java
public final class NullRoleHierarchy implements RoleHierarchy {

	@Override
	public Collection<? extends GrantedAuthority> getReachableGrantedAuthorities(
			Collection<? extends GrantedAuthority> authorities) {
		return authorities;
	}

}
```
- 참고로 RoleHierarchy 설정을 안 하면, 직접 지정한 권한 그 자체만을 반환하는 NullRoleHierarchy가 작동한다.

---

## 2. RoleHierarchy 설정
```kotlin
    @Bean
    fun roleHierarchy(): RoleHierarchy {
        val roleHierarchy = RoleHierarchyImpl()
        roleHierarchy.setHierarchy("""
            ROLE_ADMIN > ROLE_DB
            ROLE_DB > ROLE_USER
            ROLE_USER > ROLE_ANONYMOUS
        """.trimIndent())
        return roleHierarchy
    }
```
- RoleHierarchy 구현체를 빈으로 등록하면 된다.
- 구분을 할 때 개행문자를 통해 구분해야함


---


## 3. 원리

### 3.1 AuthorizeHttpRequestsConfigurer
```java
public final class AuthorizeHttpRequestsConfigurer<H extends HttpSecurityBuilder<H>>
		extends AbstractHttpConfigurer<AuthorizeHttpRequestsConfigurer<H>, H> {
    
	private final Supplier<RoleHierarchy> roleHierarchy;
	public AuthorizeHttpRequestsConfigurer(ApplicationContext context) {
        // 생략
        this.roleHierarchy = SingletonSupplier.of(() -> (context.getBeanNamesForType(RoleHierarchy.class).length > 0)
                ? context.getBean(RoleHierarchy.class) : new NullRoleHierarchy());
    }
```
- RoleHierarchy 타입 빈을 조회해서 설정으로 가지고 있다가, AuthorityAuthorizationManager 생성 시 이를 전달하여 생성

### 3.2 AuthoritiesAuthorizationManager
```java
public final class AuthoritiesAuthorizationManager implements AuthorizationManager<Collection<String>> {

    private RoleHierarchy roleHierarchy = new NullRoleHierarchy();
    
    public void setRoleHierarchy(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
    }
    
    @Override
    public AuthorityAuthorizationDecision check(Supplier<Authentication> authentication,
            Collection<String> authorities) {
        boolean granted = isGranted(authentication.get(), authorities);
        return new AuthorityAuthorizationDecision(granted, AuthorityUtils.createAuthorityList(authorities));
    }

    private boolean isGranted(Authentication authentication, Collection<String> authorities) {
        return authentication != null && isAuthorized(authentication, authorities);
    }

    private boolean isAuthorized(Authentication authentication, Collection<String> authorities) {
        for (GrantedAuthority grantedAuthority : getGrantedAuthorities(authentication)) {
            if (authorities.contains(grantedAuthority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities(Authentication authentication) {
        return this.roleHierarchy.getReachableGrantedAuthorities(authentication.getAuthorities());
    }

}
```
- 내부적으로 RoleHierarchy 를 가지고 있고, check 호출 시 해당 권한에 도달 가능한 권한들을 가져와 인가에 사용한다.

