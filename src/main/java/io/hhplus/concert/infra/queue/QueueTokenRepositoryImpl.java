package io.hhplus.concert.infra.queue;

import io.hhplus.concert.domain.queue.QueueToken;
import io.hhplus.concert.domain.queue.QueueToken.TokenStatus;
import io.hhplus.concert.domain.queue.QueueTokenRepository;
import io.hhplus.concert.support.exception.CustomNotFoundException;
import io.hhplus.concert.support.exception.ExceptionCode;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class QueueTokenRepositoryImpl implements QueueTokenRepository {
    private final QueueTokenJpaRepository queueTokenJpaRepository;

    public QueueTokenRepositoryImpl(QueueTokenJpaRepository queueTokenJpaRepository) {
        this.queueTokenJpaRepository = queueTokenJpaRepository;
    }

    @Override
    public QueueToken saveToken(QueueToken queueToken) {
        var entity = new QueueTokenEntity(queueToken);
        return this.queueTokenJpaRepository.save(entity).toDomain();
    }

    @Override
    public List<QueueToken> saveAllTokens(List<QueueToken> queueTokens) {
        var entities = queueTokens.stream().map(QueueTokenEntity::new).toList();
        return this.queueTokenJpaRepository.saveAll(entities)
            .stream().map(QueueTokenEntity::toDomain)
            .toList();
    }

    @Override
    public QueueToken getActiveTokenByUserId(long userId) {
        var entity = this.queueTokenJpaRepository.findByStatusAndUserId(TokenStatus.ACTIVE, userId);
        if(entity == null) throw new CustomNotFoundException(ExceptionCode.TOKEN_NOT_FOUND);

        return entity.toDomain();
    }

    @Override
    public QueueToken getTokenByTokenString(String token) {
        var entity = this.queueTokenJpaRepository.findByToken(token);
        if(entity == null) throw new CustomNotFoundException(ExceptionCode.TOKEN_NOT_FOUND);

        return entity.toDomain();
    }

    @Override
    public QueueToken getFirstTokenByStatus(TokenStatus tokenStatus) {
        var entity = this.queueTokenJpaRepository.findFirstByStatus(tokenStatus);
        if(entity == null) throw new CustomNotFoundException(ExceptionCode.TOKEN_NOT_FOUND);

        return entity.toDomain();
    }

    @Override
    public List<QueueToken> getTokensByStatus(TokenStatus tokenStatus, int size) {
        return this.queueTokenJpaRepository.findByStatus(tokenStatus, Limit.of(size))
            .stream().map(QueueTokenEntity::toDomain)
            .toList();
    }

    @Override
    public List<QueueToken> getActiveTokensActivatedAtBefore(Date date) {
        return this.queueTokenJpaRepository.findByActivatedAtBefore(date)
            .stream().map(QueueTokenEntity::toDomain)
            .toList();
    }

    
       
}