# SASAP!CK : 대규모 트래픽 처리 기반 온라인 쇼핑몰

![타이틀 이미지](https://github.com/user-attachments/assets/1096861f-ab73-42cb-abc2-0b950610cdee)

## 프로젝트 소개

> **SASAP!CK** 프로젝트는 **대규모 트래픽 상황**에서도 안정적인 서비스를 제공하기 위해 **마이크로서비스 아키텍처 (MSA)** 를 기반으로 설계된 쇼핑몰 시스템입니다.

- 블랙프라이데이와 같은 쇼핑 이벤트나 시즌 동안 급증하는 사용자 요청을 원활하게 처리하며 주문, 결제, 사전 예약, 검색, 재고 관리, 쿠폰 등의 핵심 쇼핑 기능을 제공하는 통합 플랫폼 서비스입니다.

- 각 서비스는 독립적으로 확장 가능하도록 설계되어, 높은 유연성을 갖추고 있습니다. 이를 통해 갑작스러운 트래픽 급증 상황에서도 신속한 응답 속도와 서비스의 안정성을 보장하여 UX를 긍정적으로 유지할 수 있습니다.

## 프로젝트 목표

- **대규모 트래픽 대응**
    - 스케일아웃을 통해 서버를 분산하여 대규모 트래픽 상황에서도 유저 요청을 안정적으로 처리
- **부하 테스트 및 성능 최적화**
    - 주요 비즈니스 API의 처리량을 200.0/sec 이상으로 달성
    - 성능 병목 구간을 분석하여 요청 처리 속도를 최적화
- **기술 선택 및 전략 고민**
    - 각 기술의 trade-off를 분석하여 서비스 요구 사항에 맞는 최적의 기술과 아키텍처 선택
    - 성능, 유지보수성, 확장성을 고려한 전략 수립
- **모니터링 및 CPU 최적화**
    - ELK 기반 모니터링 시스템으로 서버 상태와 자원 사용량을 추적
    - CPU 성능 최적화를 통해 서버 부하를 줄이고 효율성 향상
- **배포 및 운영**
    - 효율성과 일관성을 위해 Docker 및 Docker Compose 사용
    - CI/CD 파이프라인을 자동화하기 위해 GitHub Actions와 ECR 사용

## 프로젝트 멤버

| ![이미지](#)       | ![이미지](#)        | ![이미지](#)        | ![이미지](#)        |
|:------------------:|:------------------:|:------------------:|:------------------:|
| 김유연           | 이아린           | 김경민           | 김진선           |
| Leader           | Sub-Leader       | Member            | Member            |
| 상품, 사전예약주문, 모니터링 | 장바구니, 주문, CI/CD | 유저, 인증, 쿠폰, CI/CD | 결제, 검색엔진, 게이트웨이 대기열, 모니터링 |
| <a href="https://github.com/yooyouny"><img src="https://upload.wikimedia.org/wikipedia/commons/9/91/Octicons-mark-github.svg" alt="GitHub" width="20" height="20"></a> | <a href="https://github.com/eggnee"><img src="https://upload.wikimedia.org/wikipedia/commons/9/91/Octicons-mark-github.svg" alt="GitHub" width="20" height="20"></a> | <a href="https://github.com/kyeonkim"><img src="https://upload.wikimedia.org/wikipedia/commons/9/91/Octicons-mark-github.svg" alt="GitHub" width="20" height="20"></a> | <a href="https://github.com/kimzinsun"><img src="https://upload.wikimedia.org/wikipedia/commons/9/91/Octicons-mark-github.svg" alt="GitHub" width="20" height="20"></a> |

## 기술 스택

| **분류**               | **사용 기술**                                                                                     |
|------------------------|--------------------------------------------------------------------------------------------------|
| **Framework / Library** | Spring Boot, Gradle, Spring Cloud Gateway, Spring Security, Spring Data JPA, QueryDSL, Redisson, AWS Lambda |
| **Messaging**           | Apache Kafka                                                                                     |
| **DevOps**              | AWS EC2, Docker, Docker Compose, Github Actions, Kibana, LogStash, MetricBeat                     |
| **Data Source**         | AWS RDS (MySQL), AWS S3, Redis, Cassandra, ElasticSearch                                          |
| **Test**                | Apache JMeter, Postman, IntelliJ Http Client                                                     |
| **External Library**    | Slack API, Toss Payments API                                                                     |

## 배포 링크

| 항목                    | URL / 정보                                                    |
|-------------------------|---------------------------------------------------------------|
| **API Docs (Postman)**   | [API Docs (Postman)](https://documenter.getpostman.com/view/38424997/2sAY4rDjFF) | 
| **Gateway Server**       | [http://13.209.108.47:19091](http://13.209.108.47:19091)       |
| **System Monitoring (Kibana)** | [http://13.209.108.47:5601](http://13.209.108.47:5601/)  |

## 실행 방법

- 추가 예정

## 인프라 설계도

![Infra Diagram](https://github.com/user-attachments/assets/d39ac2c2-8407-4536-a5f6-b9e75f9b35a5)

## 주요 기능

- Elastic Search 기반의 상품 목록 조회 및 검색 서비스 구현
- Toss Payments 테스트 결제 모듈을 이용한 결제 서비스 구현
- Redis Sorted Set을 이용한 게이트웨이 대기열 도입
- Redis 캐싱을 활용한 장바구니/상품 카테고리 기능 구현
- Feign Client 다중 호출 기반 주문 서비스 구현
- Redis Redisson 분산 락과 Apache Kafka를 이용한 서비스 구현
    - 쿠폰 발급
    - 상품 재고 감소
    - 사전 예약 주문
- AWS S3+Lambda를 활용한 외부이미지 저장소 구축
- ELK 스택 기반의 시스템 모니터링 시스템 구축
- CI/CD Pipeline 구축에 GitHub Actions와 AWS ECR 사용

## 시스템 구조도

- **사전 예약 상품 구매 로직**
![사전예약주문](https://github.com/user-attachments/assets/8eda47aa-dfc8-45ba-bbf3-84e494d9dbf6)

- **주문-결제 로직**
![스크린샷 2024-10-23 14 12 19](https://github.com/user-attachments/assets/cca299b6-f7aa-493c-b01b-4d170f3ad21b)

- **게이트웨이 대기열**
![게이트웨이 대기열](https://github.com/user-attachments/assets/60128818-fcc0-471c-bc1b-ba7472f64a32)

- **쿠폰 발급 로직**
![쿠폰 발급 로직](https://github.com/user-attachments/assets/5603b0d5-0e48-412c-8e7c-6ca671bf47b4)

## 기술적 의사결정

1️⃣ **Elastic Search 도입으로 검색 성능 최적화**

- 기존 DB 조회 방식은 전체 테이블을 스캔하는 방식으로 속도가 느리고 대용량 데이터 처리 불가
- 대용량 데이터 처리, 실시간 색인 및 검색이 가능한 Elastic Search 도입

2️⃣ **Redis sorted set을 이용한 게이트웨이 대기열**

- 서비스의 전체적인 트래픽을 조절하기 위해 게이트웨이 대기열 도입
- proceed queue, wait queue, active queue를 만들어 사용자 활동 추적

3️⃣ **상품 저장소를 Cassandra로 선택하여 스케일아웃 상황에 유리하도록 구축** 

- 조회성능에 유리한 NoSQL 중 확장성 및 가용성이 더 좋은 카산드라를 메인 저장소로 선택
- 상품 상세조회 외 모든 조회는 ElasticSearch 기반으로 구현함으로써 카산드라의 단점을 보완

4️⃣ **Redis 기반 캐싱을 통한 장바구니 및 카테고리 조회 성능 최적화**

- Redis 도입으로 장바구니와 카테고리 데이터 캐싱을 통한 성능 최적화 및 데이터베이스 부하 감소
- 장바구니는 실시간 업데이트 및 30일 TTL 적용, 카테고리 데이터는 빠른 조회를 위한 캐싱 처리

5️⃣ **Redisson 분산 락을 활용해 쿠폰발행/재고차감/사전예약수량의 동시성 이슈를 해결**

- Redis의 분산락인 Redisson 도입으로 데이터 무결성을 유지
- 여러 서버에서 동시에 요청이 들어올 경우에도 안전하게 쿠폰발행과 재고차감, 사전예약주문을 진행

6️⃣ **Kafka를 이용하여 대규모 트래픽 처리 및 모듈 간 비동기 메시지 전송**

- 높은 처리량과 빠른 응답 속도를 제공하기 위해 비동기 처리를 위한 Kafka 도입
- 사전예약주문/쿠폰발행/결제 로직에서 활용

7️⃣ **이미지 저장과 썸네일 생성을 외부에서 처리하여 API 성능 개선**

- 이미지를 DB에 저장하면서 발생하는 오버헤드를 낮추기 위해 외부저장소인 AWS S3에서 처리
- 썸네일 생성도 서버리스인 Lambda에서 처리하여 기존 서버의 책임을 외부로 분할

## 트러블 슈팅

### 1️⃣ Elastic Search로 상품목록 조회성능 높이기

---

- 상품목록조회는 많은 필터링 조건, 정렬 조건 등으로 많은 컬럼들에 인덱싱 처리가 필요
- 데이터 인덱싱, 빠른 조회를 지원하는 Elastic Search 검색 플랫폼을 도입하여 상세조회만 카산드라에서 조회되고 모든 검색과 목록 조회는 Elastic Search 기반으로 진행되도록 구현
- Elastic Search 도입으로 상품목록 조회 API 처리량이 **약 114.3% 증가한 성과를 얻음**

### **2️⃣ 분산락과 Kafka를 사용하여 쿠폰 발급 시 발생하는 문제점 개선**

---

- 대규모 트래픽 환경에서 쿠폰 발급 시 쿠폰 수량이 공유자원이므로 동시성 이슈가 발생
- 비관적 락을 통해 동시성 문제를 해결하였으나 분산 서버에 맞지 않는다고 판단. 또한, 낮은 처리량으로 인해 분산락 + Kafka를 통해 문제점 개선
- 분산락과 Kafka 도입 후 쿠폰 발급 API 처리량이 **약 344% 이상 향상**
