# backend-lab

Spring 기반으로 백엔드/DB 동작을 “작게 만들고, 빨리 검증”하기 위한 실험 저장소입니다.  
MySQL 트랜잭션/락, Redis 원자 연산, WebSocket(STOMP) 흐름처럼 **문서만으로 이해하기 어려운 부분을 코드와 로그로 재현**하는 데 목적이 있습니다.

---

## 목표

- MySQL 동시성/트랜잭션/격리 수준/인덱스 같은 DB 이슈를 직접 재현하고 원인을 정리
- Redis의 원자 연산, Lua, 락 패턴 등을 테스트로 검증
- Spring(WebSocket/STOMP 포함) 내부 흐름을 로그 기반으로 확인
- 실험 단위로 모듈을 분리해, 필요할 때 빠르게 다시 실행 가능하게 유지

---

## 구성

> 멀티모듈(주제별)로 운영합니다.  
> 모듈 하나가 하나의 실험 단위가 되도록 유지하는 것을 원칙으로 합니다.

예시:
- `modules/mysql/*` : 트랜잭션, 락, 인덱스, 실행계획 등
- `modules/redis/*` : 원자 연산, pub/sub, 락, Lua 등
- `modules/spring/*` : WebSocket/STOMP, 트랜잭션 프록시, 이벤트 등
- `modules/cross/*` : MySQL+Redis 같이 섞이는 케이스(캐시 일관성, 락 조합 등)

---

## 실행 환경

- Java 17+
- Spring Boot 4.x
- Gradle
- MySQL / Redis (Docker 또는 Testcontainers 사용)


