package io.hhplus.concert.infra.common;

import io.hhplus.concert.domain.common.RedissonLockClient;
import io.hhplus.concert.support.exception.CustomBadRequestException;
import io.hhplus.concert.support.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedissonLockClientImpl implements RedissonLockClient {

    private final RedissonClient redissonClient;

    public RedissonLockClientImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public <T> T applyLock(String keyspace, List<String> keys, Long waitTime, Long leaseTime, RedissonLockedCallable<T> callable) {
        RLock lock;
        if (keys.size() > 1) {
            List<String> lockKeys = keys.stream()
                    .map(value -> keyspace + ":" + value)
                    .toList();
            lock = redissonClient.getMultiLock(lockKeys.stream()
                    .map(redissonClient::getLock)
                    .toArray(RLock[]::new));
        } else if(keys.size() == 1) {
            lock = redissonClient.getLock(keyspace + ":" + keys.get(0));
        } else {
            throw new CustomBadRequestException(ExceptionCode.FAIL_TO_GET_REDISSON_LOCK);
        }

        try {
            boolean isLocked = lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
            if (!isLocked) {
                throw new CustomBadRequestException(ExceptionCode.FAIL_TO_GET_REDISSON_LOCK);
            }
            return callable.call();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new CustomBadRequestException(ExceptionCode.FAIL_TO_GET_REDISSON_LOCK);
        } finally {
            // 락 해제
            lock.unlock();
        }
    }
}
