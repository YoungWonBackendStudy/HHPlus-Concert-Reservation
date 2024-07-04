# 항해 백엔드 플러스 과제 Chapter2. Concert Reservation
## 요구사항 분석 (Sequence Diagram)
### 토큰 발급 API
```mermaid
sequenceDiagram
    autonumber
    actor 사용자
    사용자 ->>+ 대기열 토큰: 토큰 요청
    Note over 사용자, 대기열 토큰: 사용자 정보
    대기열 토큰 -->>- 사용자: 대기열 토큰
```
### 대기열 Polling API
```mermaid
sequenceDiagram
    autonumber
    actor 사용자
    사용자 ->>+ 대기열 토큰: 대기열 토큰

    alt 대기열 잔류
        대기열 토큰 -->> 사용자: 대기 순서, 대기자 수
    else 대기열 통과
        대기열 토큰 -->>- 사용자: 입장 토큰, 토큰 만료 시간
    end
```
### 콘서트 조회 API
```mermaid
sequenceDiagram
    autonumber
    actor 사용자
    사용자 ->>+ 콘서트: 콘서트 날짜 조회
    콘서트 -->>- 사용자: 예약 가능한 콘서트
```

### 콘서트 날짜 조회 API
> 대기열 검증 필요
```mermaid
sequenceDiagram
    autonumber
    actor 사용자
    사용자 ->>+ 콘서트: 콘서트 날짜 조회
    Note over 사용자, 콘서트: 입장 토큰, 콘서트 정보

    콘서트 ->>+ 대기열: 입장 토큰 검증
    Note over 콘서트, 대기열: 입장 토큰
    대기열 -->>- 콘서트: 토큰 검증 결과
    break 입장 토큰 검증 실패
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
    Note over 사용자, 콘서트: 입장 토큰, 콘서트 날짜 정보

    콘서트 ->>+ 대기열: 입장 토큰 검증
    Note over 콘서트, 대기열: 입장 토큰
    대기열 -->>- 콘서트: 토큰 검증 결과
    break 입장 토큰 검증 실패
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
    사용자 ->>+ 주문: 콘서트 예약
    Note over 사용자, 주문: 토큰, 콘서트 날짜, 좌석 정보

    주문 ->>+ 대기열: 입장 토큰 검증
    Note over 주문, 대기열: 입장 토큰
    대기열 -->>- 주문: 토큰 검증 결과
    break 입장 토큰 검증 실패
        주문 -->> 사용자: 토큰 검증 실패 메세지
    end

    주문 ->> 콘서트: 좌석 예약(LOCK)
    주문 -->>- 사용자: 좌석 예약 완료
    Note over 사용자, 주문: 주문 정보
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
    Note over 사용자, 결제: 입장 토큰, 결제할 주문 정보

    결제 ->>+ 대기열: 입장 토큰 검증
    Note over 결제, 대기열: 입장 토큰
    대기열 -->>- 결제: 토큰 검증 결과
    break 입장 토큰 검증 실패
        결제 -->> 사용자: 토큰 검증 실패 메세지
    end

    결제 ->>+ 콘서트: 예약 확인
    Note over 결제, 콘서트: 사용자 정보, 좌석 번호
    콘서트 -->>- 결제: 예약 여부

    break 좌석 예약 안됨 (or 만료됨)
        결제 -->> 사용자: 좌석 예약 안됨
    end

    결제 ->>+ 잔액: 잔액 조회

    break 잔액 없음
        잔액 -->> 결제: 잔액 부족
        결제 -->> 사용자: 잔액 부족
    end

    잔액 -->>- 결제: 잔액 차감
    결제 ->> 대기열: 입장 토큰 만료
    Note over 결제, 대기열: 입장 토큰
    결제 -->>- 사용자: 예약 완료
    Note over 사용자, 결제: 주문 정보
```

### 상태 확인 및 상태값 변경
```mermaid
sequenceDiagram

Note over 스케줄러: 매 분 00초마다
스케줄러 ->>+ 대기열: 입장 토큰 확인 및 만료
스케줄러 ->>+ 주문: 주문 확인 및 예약 만료
주문 ->>+ 콘서트: 좌석 예약 취소
Note over 주문, 콘서트: 예약 취소 좌석 List
```
