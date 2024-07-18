package io.hhplus.concert.infra.waiting;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Repository;

import io.hhplus.concert.domain.waiting.WaitingToken;
import io.hhplus.concert.domain.waiting.WaitingToken.TokenStatus;
import io.hhplus.concert.support.exception.CustomNotFoundException;
import io.hhplus.concert.support.exception.ExceptionCode;
import io.hhplus.concert.domain.waiting.WaitingTokenRepository;

@Repository
public class WaitingTokenRepositoryImpl implements WaitingTokenRepository{
    private WaitingTokenJpaRepository waitingTokenJpaRepository;

    public WaitingTokenRepositoryImpl(WaitingTokenJpaRepository waitingTokenJpaRepository) {
        this.waitingTokenJpaRepository = waitingTokenJpaRepository;
    }

    @Override
    public WaitingToken saveToken(WaitingToken waitingToken) {
        var entity = new WaitingTokenEntity(waitingToken);
        return this.waitingTokenJpaRepository.save(entity).toDomain();
    }

    @Override
    public List<WaitingToken> saveAllTokens(List<WaitingToken> waitingTokens) {
        var entities = waitingTokens.stream().map(WaitingTokenEntity::new).toList();
        return this.waitingTokenJpaRepository.saveAll(entities)
            .stream().map(WaitingTokenEntity::toDomain)
            .toList();
    }

    @Override
    public WaitingToken getActiveTokenByUserId(long userId) {
        var entity = this.waitingTokenJpaRepository.findByStatusAndUserId(TokenStatus.ACTIVE, userId);
        if(entity == null) return null;

        return entity.toDomain();
    }

    @Override
    public WaitingToken getTokenByTokenString(String token) {
        var entity = this.waitingTokenJpaRepository.findByToken(token);
        if(entity == null) return null;

        return entity.toDomain();
    }

    @Override
    public WaitingToken getFirstTokenOrderByActivatedAtDesc(TokenStatus tokenStatus) {
        var entity = this.waitingTokenJpaRepository.findFirstByStatusOrderByActivatedAt(tokenStatus);
        if(entity == null) return null;

        return entity.toDomain();
    }

    @Override
    public List<WaitingToken> getTokensByStatus(TokenStatus tokenStatus, int size) {
        return this.waitingTokenJpaRepository.findByStatus(tokenStatus, Limit.of(size))
            .stream().map(WaitingTokenEntity::toDomain)
            .toList();
    }

    @Override
    public List<WaitingToken> getActiveTokensActivatedAtBefore(Date date) {
        return this.waitingTokenJpaRepository.findByActivatedAtBefore(date)
            .stream().map(WaitingTokenEntity::toDomain)
            .toList();
    }

    
       
}