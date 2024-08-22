# Redis를 이용한 대기열 구현
## 데이터 타입 및 구현 방법
  - Set
    - Member 여러개를 보관
    - 간단하게 구현 가능.
  - Sorted Set
    - Member들을 Score 값에 따라 순서를 맞춰서 저장.
    - RANGE로 특정 구간의 Member를 조회하기 편함
    - RANK로 특정 Member의 위치를 찾기 편함
  - Hash
    - Member를 Key-Value 형태로 저장할 수 있음
    - TTL을 설정할 수 있음.
    - Spring Data Redis / RedisRepository로 Java 객체 형태로 구현 가능
    - @Indexed를 통해 Indexing 가능 (Redis에는 Set형태로 Index를 관리하는 것으로 보임)

### 활성 토큰
#### Q1. 어떤 데이터 타입으로 어떻게 구현?
  - token과 userId를 통한 조회와 TTL이 필요하므로 **Redis Hash**를 사용
  - Redis Hash를 구현하기에 적합한 **Spring Data Redis와 Redis Repository**를 이용하여 구현

### 대기열 토큰
#### Q1. 어떤 데이터 타입으로 어떻게 구현?
  - TTL은 필요하지 않지만 Token/UserId를 통한 조회를 위해 **Redis Hash 및 Spring Data Redis**를 사용
#### Q2. RANK나 RANGE를 이용한 조회는...?
  - Hash에 추가로 Sorted Set를 구현.
  - 대기열에 Token이 추가될 때 Sorted Set에 Member:토큰, Score: 접속시간 형태로 SortedSet에 같이 저장하여 RANK와 RANGE를 사용할 수 있도록 설정

## 대기열 Transaction 관리
### 특정 시간당 전환하는 Token 관리
1. 1명의 User가 예약을 끝내는 데에 필요한 Transaction수는?? 6 Transactions
  - 콘서트 조회 -> 콘서트 스케줄 조회 -> 좌석 조회 -> 예약 -> 결제
  - 최소 5개 & 일부 실패에 의한 Retry를 고려하여 6 Transaction으로 예상
2. 서버가 감당할 수 있는 Transactions/1min은?? 1800Transactions/1min
  - 부하테스트를 통해 얻는 것이 정확하지만...!
  - 인터파크의 경우 1명의 유저가 좌석 구역 선택 -> 좌석 선택 -> 예약 3개의 Transaction을 발생시키고, 1초에 200명 정도 수용하는 것으로 예상됨
  - 인터파크는 3 * 10 * 60 = 1800/1min의 Transaction을 감당하는 것으로 예상
3. 그럼 우리 서버가 시간 당 들여보낼 User 수는??
  - 1800transactions/1min -> 600users/1min -> 100users/10secs
  - 1초에 100명을 들여보내면 될 것으로 생각되어 구현

### 추가로 고려해야할 사항
1. 항상 Transactions/User 와 User의 예약 시간이 동일하게 유지될까??
  - 초반에는 좌석이 많아서 충돌도 적고, 빠르게 유저가 예약하고 나갈 수 있다.
  - 하지만 뒤로갈 수록 좌석에 대한 경쟁이 심해지고, 충돌 발생 수가 증가할 것으로 예상된다
  - 잦은 충돌로 인해 Transaction/User가 증가할 수도 있고, 예약 시간 자체도 증가할 것으로 생각된다.
  - 1분당 1 유저가 발생시키는 Transaction이 증가하지 않는다면, Threshold를 두어 내부 유저수를 제한하는 것도 방법
  - Threshold를 3000 User로 설정하고 이를 넘어갈 경우 토큰을 전환하지 않는다.

### 개선할 사항
1. Threshold를 좀더 효과적으로 관리할 수 있는 방법은 없을 까...?
  - 뒤로갈 수록 충돌도 많아지고, 예약시간이 증가하는 것도 맞지만, Dead User(아무것도 하지 않는 유저)도 많아질 것으로 생각된다.
  - 초반에 순환이 잘 일어날 때는 Threshold를 낮게 잡고 시작하지만, 후반에는 Threshold가 점점 증가하는 방식으로 진행해도 괜찮을 것같다.
2. 동시성 문제
  - 은행창구 방식에서는 발생하지 않지만, 놀이공원 방식의 경우 여러개의 Application에서 Scheduler가 동작한다면 동시성 문제가 발생할 것이다.
    - (ex. 100users/10secs를 의도했지만 scheduler가 10개일 경우 1000users/10secs가 활성화될 것)
  - 1안. Scheduler를 다른 Application으로 구현하여 반드시 1개의 Scheduler만 동작하도록 설정
  - 2안. Redis를 통해 token scheduled time을 관리 (lock 이용해서 동시성 처리). 해당 시간이 10초가 지나지 않았으면 scheduling하지 않는다.
  - 2안보다는 1안이 좀 더 효율적으로 보인다.