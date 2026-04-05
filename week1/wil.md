# Spring Security & JPA 학습 정리 (1주차)

## 1. Spring Security 기초
### 핵심 개념
* **인증 (Authentication):** 사용자 본인이 맞는지 확인하는 절차 (Confirm).
* **인가 (Authorization):** 인증된 사용자가 권한이 있는지 확인.

### Servlet Filter
* **정의:** 보안을 담당하는 중간 필터 유닛.
* **역할:** HTTP 요청(Request)과 응답(Response)을 중간에 가로채서 전처리 및 후처리를 수행하는 장치.
* **특징:** Spring Security 활성화 시 기본적으로 로그인 페이지로 리다이렉트되며, 적절한 설정 없이는 기존의 CRUD 작업이 불가능해짐.

---

## 2. Spring Data JPA 활용
### JpaRepository 상속
* 인터페이스 선언 시 `JpaRepository<Entity, ID>`를 상속받으면 기본적인 메서드들이 자동으로 제공됨.
* **주요 메서드:** `findAll()`, `save()`, `findById()`, `delete()` 등.

### JPA Query Method
* 메서드 이름을 규칙에 맞게 명명하면 실행 시점에 자동으로 SQL 쿼리를 생성해주는 기능.
* **여러가지 메서드 네이밍 기술된 문서:** [Spring Data JPA 공식 문서](https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html#jpa.query-methods.query-creation)
* **예시:** `findByUsername(String username)`과 같이 정의하여 특정 필드로 조회 가능.

---

## 3. Optional Type (Null 안정성)
JPA는 데이터가 없을 경우 `null`을 직접 반환하는 대신, `Optional`로 감싸서 반환하여 `NullPointerException`을 방지함.

* **값 추출:** `member.get()`을 통해 내부 객체를 꺼낼 수 있음.
* **예외 처리:** `.orElseThrow()`를 사용하여 값이 없을 경우 즉시 예외를 발생시킴.
* **람다식 활용 예시:**
```java
memberRepository.findByUsername(name)
.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
