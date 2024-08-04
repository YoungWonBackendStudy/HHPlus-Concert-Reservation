package io.hhplus.concert.infra.queue;

import io.hhplus.concert.domain.queue.WaitingQueueToken;
import io.hhplus.concert.domain.queue.WaitingQueueTokenRepository;
import io.hhplus.concert.support.config.MyRedisKeyspaceConfig;
import io.hhplus.concert.support.exception.CustomNotFoundException;
import io.hhplus.concert.support.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WaitingQueueTokenRepositoryImpl implements WaitingQueueTokenRepository {
    private final WaitingQueueTokenRedisRepository waitingQueueTokenRedisRepository;
    private final RedisTemplate<String, String> waitingQueueTokenRedisTemplate;
    private final MyRedisKeyspaceConfig keyspace;

    @Override
    public WaitingQueueToken saveWaitingQueueToken(WaitingQueueToken token) {
        var entity = new WaitingQueueTokenEntity(token);
        waitingQueueTokenRedisTemplate.opsForZSet().add(keyspace.getWaitingToken(), token.getToken(), token.getIssuedAtInMillis());
        return this.waitingQueueTokenRedisRepository.save(entity).toDomain();
    }

    @Override
    public Long getWaitingQueueTokenRank(WaitingQueueToken token) {
        var rank =  waitingQueueTokenRedisTemplate.opsForZSet().rank(keyspace.getWaitingToken(), token.getToken());
        if(rank == null) throw new CustomNotFoundException(ExceptionCode.WAITING_TOKEN_NOT_FOUND);

        return rank;
    }

    @Override
    public List<WaitingQueueToken> dequeFirstNWaitingQueueTokens(Long sizeN) {
        var tokens = waitingQueueTokenRedisTemplate.opsForZSet().range(keyspace.getWaitingToken(), 0L, sizeN).stream().toList();
        if(tokens.isEmpty()) return List.of();
        var tokenEntities = tokens.stream().map(token -> {
            var tokenEntity = waitingQueueTokenRedisRepository.findById(token).orElse(null);
            if(tokenEntity == null) throw new CustomNotFoundException(ExceptionCode.WAITING_TOKEN_NOT_FOUND);

            return tokenEntity;
        }).toList();

        waitingQueueTokenRedisRepository.deleteAll(tokenEntities);
        waitingQueueTokenRedisTemplate.opsForZSet().removeRange(keyspace.getWaitingToken(), 0L, sizeN);

        return tokenEntities.stream().map(WaitingQueueTokenEntity::toDomain).toList();
    }

    @Override
    public WaitingQueueToken getWaitingQueueTokenByUserId(long userId) {
        var entity = this.waitingQueueTokenRedisRepository.findByUserId(userId);
        if(entity == null) throw new CustomNotFoundException(ExceptionCode.WAITING_TOKEN_NOT_FOUND);

        return entity.toDomain();
    }

    @Override
    public void deleteWaitingQueueToken(WaitingQueueToken token) {

    }
}