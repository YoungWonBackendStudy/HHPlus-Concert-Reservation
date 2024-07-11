# 항해 백엔드 플러스 과제 Chapter2. Concert Reservation
## 요구사항 분석 (Sequence Diagram)
### 토큰 발급 API
```mermaid
sequenceDiagram
    autonumber
    actor 사용자
    사용자 ->>+ 토큰: 토큰 요청
    Note over 사용자, 토큰: 사용자 정보
    토큰 -->>- 사용자: 토큰, 대기 순서
```

### 콘서트 조회 API
> 대기열 검증 필요
```mermaid
sequenceDiagram
    autonumber
    actor 사용자
    사용자 ->>+ 콘서트: 콘서트 날짜 조회
    Note over 사용자, 콘서트: 토큰

    콘서트 ->>+ 대기열: 토큰 검증
    Note over 콘서트, 대기열: 토큰
    대기열 -->>- 콘서트: 토큰 검증 결과
    break 토큰 검증 실패
        콘서트 -->> 사용자: 토큰 검증 실패 메세지
    end

    콘서트 -->>- 사용자: 예약 가능한 콘서트
```

### 콘서트 날짜 조회 API
> 대기열 검증 필요
```mermaid
sequenceDiagram
    autonumber
    actor 사용자
    사용자 ->>+ 콘서트: 콘서트 날짜 조회
    Note over 사용자, 콘서트: 토큰, 콘서트 정보

    콘서트 ->>+ 대기열: 토큰 검증
    Note over 콘서트, 대기열: 토큰
    대기열 -->>- 콘서트: 토큰 검증 결과
    break 토큰 검증 실패
        콘서트 -->> 사용자: 토큰 검증 실패 메세지
    end

    콘서트 -->>- 사용자: 예약 가능한 날짜
```

### 콘서트 좌석 조회 API
> 대기열 검증 필요
```mermaid
sequenceDiagram
    autonumber
    actor 사용자
    사용자 ->>+ 콘서트: 콘서트 좌석 조회
    Note over 사용자, 콘서트: 토큰, 콘서트 날짜 정보

    콘서트 ->>+ 대기열: 토큰 검증
    Note over 콘서트, 대기열: 토큰
    대기열 -->>- 콘서트: 토큰 검증 결과
    break 토큰 검증 실패
        콘서트 -->> 사용자: 토큰 검증 실패 메세지
    end

    콘서트 -->>- 사용자: 해당 날짜의 예약 가능한 좌석번호
```

### 콘서트 좌석 예약 API
> 대기열 검증 필요
```mermaid
sequenceDiagram
    autonumber
    actor 사용자
    사용자 ->>+ 예약: 콘서트 예약
    Note over 사용자, 예약: 토큰, 콘서트 날짜, 좌석 정보

    예약 ->>+ 대기열: 토큰 검증
    Note over 예약, 대기열: 토큰
    대기열 -->>- 예약: 토큰 검증 결과
    break 토큰 검증 실패
        예약 -->> 사용자: 토큰 검증 실패 메세지
    end

    예약 -->>- 사용자: 좌석 예약 완료
    Note over 사용자, 예약: 예약 정보
```

### 잔액 조회 API
```mermaid
sequenceDiagram
    autonumber
    actor 사용자

    사용자 ->>+ 잔액: 잔액 조회
    Note over 사용자, 잔액: 사용자 정보

    잔액 -->>- 사용자: 잔액
```

### 잔액 충전 API
```mermaid
sequenceDiagram
    autonumber
    actor 사용자
    사용자 ->>+ 잔액: 잔액 충전
    Note over 사용자, 잔액: 사용자 정보, 충전할 금액

    잔액 -->>- 사용자: 충전 후 잔액
```

### 결제 API
> 대기열 검증 필요
```mermaid
sequenceDiagram
    autonumber
    actor 사용자

    사용자 ->>+ 결제: 결제 요청
    Note over 사용자, 결제: 토큰, 예약 정보

    결제 ->>+ 대기열: 토큰 검증
    Note over 결제, 대기열: 토큰
    대기열 -->>- 결제: 토큰 검증 결과
    break 토큰 검증 실패
        결제 -->> 사용자: 토큰 검증 실패 메세지
    end

    결제 ->>+ 예약: 좌석 예약 확인
    Note over 결제, 예약: 예약 정보
    예약 -->>- 결제: 좌석 예약 여부

    break 좌석 예약 안됨 (or 만료됨)
        결제 -->> 사용자: 좌석 예약 안됨
    end

    결제 ->>+ 잔액: 잔액 차감

    break 잔액 없음
        잔액 -->> 결제: 잔액 부족
        결제 -->> 사용자: 잔액 부족
    end

    잔액 -->>- 결제: 잔액 차감 완료
    결제 ->> 대기열: 토큰 만료
    Note over 결제, 대기열: 토큰
    결제 -->>- 사용자: 결제 완료
    Note over 사용자, 결제: 결제 정보
```

### 상태 확인 및 상태값 변경
```mermaid
sequenceDiagram

Note over 스케줄러: 매 분 00초마다
스케줄러 ->>+ 대기열: 토큰 확인 및 만료
스케줄러 ->>+ 예약: 예약 확인 및 만료
예약 ->>+ 콘서트: 좌석 예약 취소
Note over 예약, 콘서트: 예약 취소 좌석 List
```
## Milestone
### Tasks
- 설계
    - 요구사항 분석
    - ERD 설계
    - API 명세 작성 및 MockAPI 구현
- 대기열
    - 토큰 발급
    - 토큰 만료 스케줄러
- 콘서트
    - 콘서트 날짜/좌석 조회 API
- 예약
    - 좌석 예약 API
- 결제
    - 잔액 조회/충전 API
    - 결제 API
- 배포
    - 배포 환경 설정 및 테스트

### Gantt Chart

- 3주차
```mermaid
gantt
dateFormat YYYY-MM-DD
Section 설계
    Milestone       : 2024-06-29, 1d
    요구사항 분석(SSD): 2024-06-29, 2d
    ERD 설계       : 2024-06-30, 1d
    API 명세 설계   : 2024-07-01, 1d
    환경 설정 및 MockAPI:    2024-07-02, 1d
    설계 검토 및 마무리: 2024-07-03, 2d
```

 - 4주차
```mermaid
gantt
dateFormat YYYY-MM-DD

Section 토큰 발급
    개발           : dev_get_tok, 2024-07-06, 6h
    테스트/오류수정 : test_get_tok, after dev_get_tok, 6h
Section 날짜/좌석 조회
    개발           : dev_get_con, after test_get_tok, 6h
    테스트/오류수정 : test_get_con, after dev_get_con, 6h
Section 좌석 예약 ( + 임시 배정/만료)
    개발           : dev_reserve, after test_get_con, 12h
    테스트/오류수정 : test_reserve, after dev_reserve, 12h
Section 잔액 조회/충전
    개발           : dev_asset, after test_reserve, 6h
    테스트/오류수정 : test_asset, after dev_asset, 6h
Section 결제
    개발           : dev_pay, after test_asset, 12h
    테스트/오류수정 : test_pay, after dev_pay, 12h
Section 대기열 만료 스케줄링
    개발           : dev_waiting_sch, after test_tok_val, 12h
    테스트/오류수정 : test_waiting_sch, after dev_waiting_sch, 12h
```

 - 5주차
```mermaid
gantt
dateFormat YYYY-MM-DD
Section 배포
    배포 환경 구축  : env_setting, 2024-07-13, 2d
    배포 및 테스트  : env_extra, after env_setting, 1d
```


## ERD
```mermaid
erDiagram

User {
    long user_id PK
}

User ||--|| Asset: has
Asset {
    long id PK
    long user_id FK
    long balance
}

User ||--o{ Reservation: reserves
Reservation {
    long id PK
    long user_id FK
    string status "Index"
    date reserved_at
    date completed_at
}

Reservation ||--|{ ReservationTicket: contains
ReservationTicket {
    long id PK
    long reservation_id FK
    long concert_seat_id FK
    string seat_location
    long price
}

Reservation ||--o| Payment: "1:0..1"
Payment {
    long id PK
    long reservation_id FK
    long price
    date paid_at
}

Concert {
    long id PK
    string name
    string description
}

Concert ||--|{ ConcertSchedule : contains
ConcertSchedule {
    long id PK
    long concert_id FK
    string place
    date reservation_st_date
    date reservation_end_date
    date concert_date
}

ConcertSchedule ||--|{ ConcertSeat : contains
ConcertSeat {
    long id PK
    long concert_schedule_id FK
    string location
    long price
}

WaitingToken {
    long id PK "AUTO_INCREMENT"
    string token UK
    long user_id FK
    enum status
    date issued_at
    date activated_at
    date deleted_at
}
```
## API 명세
### GET /waiting/token (토큰 발급)
- Request

|pos|name|type|description|
|---|---|---|---|
|Param|userId|long|사용자 ID|

- Response
```json
{
    "waitingId": long,
    "waitingToken": string,
    "precedingWaiting": long
}
```

### GET /concerts (Concert 조회)
- Request

- Response

```json
[
    {
        "id": long,
        "name": string,
        "description": string
    }
]
```

### GET /concerts/schedules (Concert 스케줄 조회)
- Request

|pos|name|type|description|
|---|---|---|---|
|Param|passToken|string|토큰|
|Param|concertId|long|콘서트 ID|

- Response

```json
[
    {
        "id": long,
        "date": date,
        "address": string
    }
]
```

### GET /concerts/schedules/seats (예약 가능 좌석 조회 API)
- Request

|pos|name|type|description|
|---|---|---|---|
|Param|passToken|string|토큰|
|Param|concertScheduleId|long|콘서트 일정 ID|

- Response Body

```json
[
    {
        "id": long,
        "location": string,
        "price": long
    }
]
```

### POST /reservations (좌석 예약 API)
- Request

|pos|name|type|description|
|---|---|---|---|
|Param|passToken|string|토큰|

- Request Body
```json
{
    seatIds: [ long ]
}
```

- Response Body

```json
{
    "reservationId": long,
    "totalPrice": long,
    "reservedAt": date,
    "expiresAt": date,
    "reservedSeats": [
        {
            "id": long,
            "location": string,
            "price": long
        }
    ]
}
```

### GET /asset (잔액 조회 API)
- Request

|pos|name|type|description|
|---|---|---|---|
|Param|userId|long|사용자 ID|

- Response Body

```json
{
    "balance": long
}
```

### PATCH /asset/charge (잔액 충전 API)
- Request

|pos|name|type|description|
|---|---|---|---|
|Param|userId|long|사용자 ID|

- Request Body
```json
{
    "amount": long
}
```

- Response Body
```json
{
    "balance": long
}
```

### POST /payment (결제 API)
- Request

|pos|name|type|description|
|---|---|---|---|
|Param|passToken|string|Opaque 토큰|

- Request Body
```json
{
    "reservationId": long
}
```

- Response Body

```json
{
    "paidAmount": long,
    "balance": long
}
```
