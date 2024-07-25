package io.hhplus.concert.infra.queue;

import io.hhplus.concert.domain.queue.WaitingQueueToken;
import io.hhplus.concert.domain.queue.WaitingQueueToken.TokenStatus;
import io.hhplus.concert.domain.queue.WaitingQueueTokenRepository;
import io.hhplus.concert.support.exception.CustomNotFoundException;
import io.hhplus.concert.support.exception.ExceptionCode;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class WaitingQueueTokenRepositoryImpl implements WaitingQueueTokenRepository {
    private final WaitingQueueTokenJpaRepository waitingQueueTokenJpaRepository;

    public WaitingQueueTokenRepositoryImpl(WaitingQueueTokenJpaRepository waitingQueueTokenJpaRepository) {
        this.waitingQueueTokenJpaRepository = waitingQueueTokenJpaRepository;
    }

    @Override
    public WaitingQueueToken saveToken(WaitingQueueToken waitingQueueToken) {
        var entity = new WaitingQueueTokenEntity(waitingQueueToken);
        return this.waitingQueueTokenJpaRepository.save(entity).toDomain();
    }

    @Override
    public List<WaitingQueueToken> saveTokens(List<WaitingQueueToken> waitingQueueTokens) {
        var entities = waitingQueueTokens.stream().map(WaitingQueueTokenEntity::new).toList();
        return this.waitingQueueTokenJpaRepository.saveAll(entities)
                .stream().map(WaitingQueueTokenEntity::toDomain).toList();
    }

    @Override
    public WaitingQueueToken getActiveTokenByUserId(long userId) {
        var entity = this.waitingQueueTokenJpaRepository.findByStatusAndUserId(TokenStatus.ACTIVE, userId);
        if(entity == null) throw new CustomNotFoundException(ExceptionCode.TOKEN_NOT_FOUND);

        return entity.toDomain();
    }

    @Override
    public WaitingQueueToken getTokenByTokenString(String token) {
        var entity = this.waitingQueueTokenJpaRepository.findByToken(token);
        if(entity == null) throw new CustomNotFoundException(ExceptionCode.TOKEN_NOT_FOUND);

        return entity.toDomain();
    }

    @Override
    public WaitingQueueToken getFirstTokenByStatus(TokenStatus tokenStatus) {
        var entity = this.waitingQueueTokenJpaRepository.findFirstByStatus(tokenStatus);
        if(entity == null) throw new CustomNotFoundException(ExceptionCode.TOKEN_NOT_FOUND);

        return entity.toDomain();
    }

    @Override
    public List<WaitingQueueToken> getTokensByStatus(TokenStatus tokenStatus, int size) {
        return this.waitingQueueTokenJpaRepository.findByStatus(tokenStatus, Limit.of(size))
            .stream().map(WaitingQueueTokenEntity::toDomain)
            .toList();
    }

    @Override
    public List<WaitingQueueToken> getActiveTokensActivatedAtBefore(Date date) {
        return this.waitingQueueTokenJpaRepository.findByActivatedAtBefore(date)
            .stream().map(WaitingQueueTokenEntity::toDomain)
            .toList();
    }

    
       
}