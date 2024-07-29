package io.hhplus.concert.domain.concert;

public interface ReservationRedissonClient {
    ReservationLock tryGetConcertSeatLockFor1s(Long seatId);
    void releaseLock(ReservationLock lock);
}
