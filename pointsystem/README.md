# Point System

포인트 시스템은 사용자의 포인트를 관리하고, 전자책 열람 시 구독 상태에 따라 포인트를 차감하는 서비스입니다.

## 주요 기능

### 1. 회원가입 시 포인트 지급
- **KT 회원**: 5,000 포인트 지급
- **NORMAL 회원**: 1,000 포인트 지급

### 2. 전자책 열람 시 포인트 차감
- **구독자 (SUBSCRIBED)**: 0 포인트 차감
- **일반 사용자**: 전자책 가격만큼 포인트 차감

## API 엔드포인트

### 사용자 포인트 조회
```
GET /userPoints/{userId}
```

### 전자책 열람 (포인트 차감)
```
POST /userPoints/viewEbook
Content-Type: application/json

{
  "userId": "user123",
  "ebookId": "ebook456",
  "price": 500
}
```

## Kafka 이벤트 처리

### SignedUp 이벤트
- 멤버십 타입에 따른 포인트 자동 지급
- Topic: `labcqrssummarize`

### HandleEBookViewed 이벤트
- 전자책 열람 이벤트 로깅
- Topic: `labcqrssummarize`

## 외부 서비스 연동

### Subscriber 서비스
- Feign Client를 통한 구독 상태 조회
- URL: `localhost:8081` (개발환경), `subscriber:8080` (Docker)

## 실행 방법

```bash
# 개발환경
mvn spring-boot:run

# Docker
docker build -t pointsystem .
docker run -p 8087:8087 pointsystem
```

## 포트
- **8087**: Pointsystem 서비스