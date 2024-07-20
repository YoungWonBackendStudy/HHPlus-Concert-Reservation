package io.hhplus.concert.infra.waiting;

import java.util.List;
import java.util.Date;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import io.hhplus.concert.domain.waiting.WaitingToken.TokenStatus;

public interface WaitingTokenJpaRepository extends JpaRepository<WaitingTokenEntity, Long>{
    public WaitingTokenEntity findByStatusAndUserId(TokenStatus status, long userId);
    public WaitingTokenEntity findByToken(String tokenStr);
    public WaitingTokenEntity findFirstByStatusOrderByActivatedAt(TokenStatus status);
    public List<WaitingTokenEntity> findByStatus(TokenStatus status, Limit limit);
    public List<WaitingTokenEntity> findByActivatedAtBefore(Date date);
}
