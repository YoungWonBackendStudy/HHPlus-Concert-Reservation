package io.hhplus.concert.infra.queue;

import java.util.List;
import java.util.Date;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import io.hhplus.concert.domain.queue.QueueToken.TokenStatus;

public interface QueueTokenJpaRepository extends JpaRepository<QueueTokenEntity, Long>{
    public QueueTokenEntity findByStatusAndUserId(TokenStatus status, long userId);
    public QueueTokenEntity findByToken(String tokenStr);
    public QueueTokenEntity findFirstByStatusOrderByActivatedAt(TokenStatus status);
    public List<QueueTokenEntity> findByStatus(TokenStatus status, Limit limit);
    public List<QueueTokenEntity> findByActivatedAtBefore(Date date);
}
