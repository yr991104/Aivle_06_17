# 

## Model
www.msaez.io/#/163964407/storming/labcqrs-2310223

## Before Running Services
### Make sure there is a Kafka server running
```
cd kafka
docker-compose up
```
- Check the Kafka messages:
```
cd infra
docker-compose exec -it kafka /bin/bash
cd /bin
./kafka-console-consumer --bootstrap-server localhost:9092 --topic
```

## Run the backend micro-services
See the README.md files inside the each microservices directory:

- manageauthor
- adminsystem
- ebookplatform
- subscriber
- aisystem
- pointsystem


## Run API Gateway (Spring Gateway)
```
cd gateway
mvn spring-boot:run
```

## Test by API
- manageauthor
```
 http :8088/authors authorId="authorId"name="name"isApproved="isApproved"ebooks="ebooks"userId="userId"
```
- adminsystem
```
 http :8088/authors authorId="authorId"name="name"isApproved="isApproved"ebooks="ebooks"userId="userId"
 http :8088/eBooks ebookId="ebookId"title="title"authorId="authorId"content="content"coverImage="coverImage"summary="summary"price="price"category="category"countViews="countViews"
```
- ebookplatform
```
 http :8088/eBookPlatforms pid="pid"ebooks="ebooks"registeredAt="registeredAt"coverImage="coverImage"summary="summary"prices="prices"
```
- subscriber
```
 http :8088/subscribers subscriberId="subscriberId"userId="userId"subscriptionType="subscriptionType"startedAt="startedAt"expiredAt="expiredAt"password="password"
```
- aisystem
```
 http :8088/eBooks ebookId="ebookId"title="title"authorId="authorId"content="content"coverImage="coverImage"summary="summary"isPublicationApproved="isPublicationApproved"price="price"category="category"countViews="countViews"
```
- pointsystem
```
 http :8088/userPoints userId="userId"point="point"
```


## Run the frontend
```
cd frontend
npm i
npm run serve
```

## Test by UI
Open a browser to localhost:8088

## Required Utilities

- httpie (alternative for curl / POSTMAN) and network utils
```
sudo apt-get update
sudo apt-get install net-tools
sudo apt install iputils-ping
pip install httpie
```

- kubernetes utilities (kubectl)
```
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
```

- aws cli (aws)
```
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install
```

- eksctl 
```
curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin
```


# 서비스 기본 API 사용 가이드

이 문서는 회원가입부터 포인트 조회, 전자책 등록, 열람, 포인트 재조회까지 전체 흐름에 대한 API 사용법을 안내합니다.

---

## 1. 회원가입

- 사용자 아이디, 비밀번호, 이메일, 멤버십 타입(KT 혹은 일반)을 등록합니다.
- 구독 타입은 포함하지 않습니다.

### 요청 예시

```bash
curl -X POST http://localhost:8085/subscribers/register \
-H "Content-Type: application/json" \
-d '{
  "userId": "testuser7",
  "password": "password123",
  "email": "testuser7@example.com",
  "membershipType": "KT"
}'
```


## 2. 포인트 조회
```bash
curl -X GET http://localhost:8087/userPoints/testuser7
```


## 3. 전자책 등록(테스트용)
```bash
curl -X POST http://localhost:8084/ebooks \
-H "Content-Type: application/json" \
-d '{
    "pid": 1001,
    "ebooks": ["Test Book Title"],
    "coverImage": "http://example.com/image.png",
    "summary": "This is a test summary",
    "price": 3000
}'
```


## 4. 전자책 열람요청
```bash
curl -X POST http://localhost:8085/subscribers/{subscriberId}/open \
-H "Content-Type: application/json" \
-d '{
  "ebookId": "1001"
}'
```


## 5.포인트 재조회
```bash
curl -X GET http://localhost:8087/userPoints/testuser7
```


