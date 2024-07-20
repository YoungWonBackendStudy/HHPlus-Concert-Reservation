package io.hhplus.concert.infra.queue;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Repository;

import io.hhplus.concert.domain.queue.QueueToken;
import io.hhplus.concert.domain.queue.QueueToken.TokenStatus;
import io.hhplus.concert.domain.queue.QueueTokenRepository;

@Repository
public class QueueTokenRepositoryImpl implements QueueTokenRepository {
    private QueueTokenJpaRepository queueTokenJpaRepository;

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
        if(entity == null) return null;

        return entity.toDomain();
    }

    @Override
    public QueueToken getTokenByTokenString(String token) {
        var entity = this.queueTokenJpaRepository.findByToken(token);
        if(entity == null) return null;

        return entity.toDomain();
    }

    @Override
    public QueueToken getFirstTokenOrderByActivatedAtDesc(TokenStatus tokenStatus) {
        var entity = this.queueTokenJpaRepository.findFirstByStatusOrderByActivatedAt(tokenStatus);
        if(entity == null) return null;

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