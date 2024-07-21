package io.hhplus.concert.infra.queue;

import java.util.List;
import java.util.Date;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import io.hhplus.concert.domain.queue.QueueToken.TokenStatus;

public interface QueueTokenJpaRepository extends JpaRepository<QueueTokenEntity, Long>{
    QueueTokenEntity findByStatusAndUserId(TokenStatus status, long userId);
    QueueTokenEntity findByToken(String tokenStr);
    QueueTokenEntity findFirstByStatus(TokenStatus status);
    List<QueueTokenEntity> findByStatus(TokenStatus status, Limit limit);
    List<QueueTokenEntity> findByActivatedAtBefore(Date date);
}
