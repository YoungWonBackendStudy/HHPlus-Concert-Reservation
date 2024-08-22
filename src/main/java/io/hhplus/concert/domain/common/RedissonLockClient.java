package io.hhplus.concert.domain.common;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RedissonLockClient {
    interface RedissonLockedCallable<T> {
        @Transactional(propagation = Propagation.REQUIRES_NEW)
        T call();
    }

    <T> T applyLock(String keyspace, List<String> keys, Long waitTime, Long leaseTime, RedissonLockedCallable<T> callable);
}
