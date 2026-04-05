# Spring Security (2주차)

## 1. Spring Security 자동 설정의 원리
### @ConditionalOnDefaultWebSecurity
* **동작 방식**: 스프링 부트는 `SpringBootWebSecurityConfiguration`을 통해 기본적인 보안 설정을 제공
* **조건부 생성**: `@ConditionalOnDefaultWebSecurity` 어노테이션은 개발자가 직접 `SecurityFilterChain` 빈을 등록하지 않았을 때만 기본 로그인 페이지와 모든 경로 차단 로직을 활성화
* **커스텀 설정**: 직접 `WebSecurityConfig`를 작성하여 `SecurityFilterChain`을 빈으로 등록하면 기본 설정은 비활성화되고 직접 정의한 부분이 활성화됨

### HttpSecurity의 빈 스코프
* **Prototype Scope**: `HttpSecurity` 빈은 싱글톤이 아닌 프로토타입 스코프로 관리
* **이유**: 여러 개의 `SecurityFilterChain`을 구성할 때 각 필터 체인이 상태를 공유하지 않게하기 위함

## 2. 세션 기반 인증 (Stateful)
### JSESSIONID와 세션의 원리
* **개념**: 서버가 사용자의 인증 상태를 저장하는 **스테이풀(Stateful)** 방식
* **메커니즘**: 인증에 성공하면 서버는 세션을 생성하고 고유한 **JSESSIONID**를 쿠키에 담아 발급
* **인증 유지**: 클라이언트는 이후 요청마다 이 쿠키를 함께 전송하며, 서버는 세션 저장소에서 해당 ID와 매칭되는 인증 정보(`SecurityContext`)를 찾아 사용자를 식별

### 인증 플로우 (5단계)
1. **토큰 변환**: 로그인 요청 정보를 `UsernamePasswordAuthenticationToken` 객체로 변환
2. **인증 시도**: `AuthenticationManager`를 통해 DB의 사용자 정보와 비밀번호를 검증
3. **Context 저장**: 인증 성공 시 `SecurityContextHolder`에 인증 객체를 설정 
4. **세션 생성**: `HttpServletRequest.getSession(true)`를 통해 세션을 생성하고 JSESSIONID를 발급 
5. **인증 정보 연동**: 생성된 세션에 `SecurityContext`를 저장하여 로그인 상태를 유지 

## 3. ROLE 접두사
### MemberRole과 인가(Authorization)
* **역할과 권한의 구분**: 스프링 시큐리티는 ROLE로 사용사즤 권한을 관리
* **ROLE_ 접두사 규칙**:
    * **DB/Enum 저장**: 시큐리티의 규칙상 ROLE_ 접두사를 붙여야함 ex)`ROLE_ADMIN`
    * **설정 파일**: `WebSecurityConfig`에서 `.hasRole("REGULAR")`와 같이 사용할 때는 `ROLE_`을 생략해도 시큐리티가 알아서 `ROLE_` 붙여서 검사
