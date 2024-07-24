package io.hhplus.concert.infra.queue;

import java.util.List;
import java.util.Date;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import io.hhplus.concert.domain.queue.WaitingQueueToken.TokenStatus;

public interface WaitingQueueTokenJpaRepository extends JpaRepository<WaitingQueueTokenEntity, Long>{
    WaitingQueueTokenEntity findByStatusAndUserId(TokenStatus status, long userId);
    WaitingQueueTokenEntity findByToken(String tokenStr);
    WaitingQueueTokenEntity findFirstByStatus(TokenStatus status);
    List<WaitingQueueTokenEntity> findByStatus(TokenStatus status, Limit limit);
    List<WaitingQueueTokenEntity> findByActivatedAtBefore(Date date);
}
