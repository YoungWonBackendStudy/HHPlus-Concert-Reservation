package io.hhplus.concert.infra.concert;

import io.hhplus.concert.domain.concert.ReservationLock;
import io.hhplus.concert.domain.concert.ReservationRedissonClient;
import io.hhplus.concert.support.exception.CustomBadRequestException;
import io.hhplus.concert.support.exception.ExceptionCode;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ReservationRedissonClientImpl implements ReservationRedissonClient {
    RedissonClient redissonClient;

    public ReservationRedissonClientImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public ReservationLock tryGetConcertSeatLockFor1s(Long seatId) {
        RLock lock = redissonClient.getLock(seatId.toString());
        boolean isLock = false;
        try{
            isLock = lock.tryLock(0, 1, TimeUnit.SECONDS);
        } catch(InterruptedException ignored) {
        }

        if(!isLock) throw new CustomBadRequestException(ExceptionCode.RESERVATION_FAIL_TO_GET_REDISSON_LOCK);

        return new ReservationLock(lock, seatId);
    }

    @Override
    public void releaseLock(ReservationLock lock) {
        lock.getLock().unlock();
    }
}
