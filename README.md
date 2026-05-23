# CatchPro 🎯
> **대용량 트래픽 및 피크 타임 부하 처리를 위한 인기 전문가 실시간 상담 예약 시스템**

인기 전문가의 상담 예약 오픈 시 발생하는 대용량 트래픽을 안정적으로 처리하고, 실시간 예약 과정에서 발생하는 동시성 문제를 해결하여 사용자에게 신뢰성 있는 서비스를 제공하는 백엔드 시스템입니다.

---

## 🛠️ Tech Stack & Environment

### Backend
- **Language:** Java 25
- **Framework:** Spring Boot 4.0.6
- **Database:** MySQL 8.0, Spring Data JPA
- **Cache & Lock:** Redis
- **Message Queue:** Apache Kafka (KRaft Mode)
- **API Docs:** Springdoc OpenAPI (Swagger UI)

### Infrastructure & DevOps
- **Containerization:** Docker, Docker Compose
- **Monitoring:** Prometheus, Grafana, Spring Boot Actuator

---

## 🏗️ System Architecture

- 클라이언트 요청을 Spring Boot 서버에서 처리하며, **MySQL**을 메인 DB로 사용합니다.
- **Redis**를 활용해 피크 타임 예약 시 발생하는 동시성 이슈를 제어합니다 (분산 락/캐싱).
- **Kafka** 메시지 큐를 도입하여 순간적으로 몰리는 예약 요청(대용량 트래픽)을 비동기적으로 처리하고 부하를 분산시킵니다.
- **Prometheus**와 **Grafana**를 통해 시스템 메트릭(TPS, CPU, Memory 등)을 실시간으로 수집하고 시각화합니다.

---

## 🔥 Key Features & Troubleshooting

### 1. 피크 타임 대용량 트래픽 처리 (Kafka)
- 인기 전문가 예약 등 순간적인 트래픽 폭증 상황에서 서버 다운을 막기 위해 **Kafka**를 대기열 및 비동기 처리 시스템으로 활용했습니다.
- DB 커넥션 병목을 방지하기 위해 HikariCP Maximum Pool Size를 최적화(20)하여 대량의 요청을 안정적으로 소화합니다.

### 2. 예약 시스템 동시성 제어 (Redis)
- 다수의 사용자가 동일한 상담 시간에 예약을 시도할 때 발생하는 중복 예약(Overbooking) 문제를 방지하기 위해 **Redis**를 활용한 동시성 제어 로직을 적용했습니다.

### 3. 실시간 모니터링 및 성능 최적화 (Prometheus & Grafana)
- `Spring Boot Actuator`와 `Prometheus`를 연동하여 애플리케이션의 헬스 체크 및 주요 메트릭을 수집합니다.
- 수집된 데이터를 **Grafana** 대시보드로 시각화하여, 부하 발생 시 병목 지점을 즉각적으로 파악하고 성능 개선의 지표로 활용합니다.

---

## 📁 Project Structure (주요 파일 구성)
```text
├── src/main/java/junsang/cho/catchpro/ # 메인 애플리케이션 소스
├── src/main/resources/application.yml  # 애플리케이션, DB, Kafka, Redis 설정
├── build.gradle                        # 의존성 관리 및 빌드 설정
├── docker-compose.yml                  # 인프라(MySQL, Redis, Kafka, Prometheus, Grafana) 구축
└── prometheus.yml                      # 프로메테우스 메트릭 수집 설정
```
---

## 🚀 Getting Started
### Prerequisites

- Docker & Docker Compose
- Java 25
- Gradle

### Installation & Run

```
# 1. 저장소 복사
git clone [https://github.com/junsangCho/CatchPro.git](https://github.com/junsangCho/CatchPro.git)

# 2. 인프라 환경 실행 (MySQL, Redis, Kafka, Prometheus, Grafana)
docker-compose up -d

# 3. 애플리케이션 빌드 및 실행
./gradlew bootRun
```
---