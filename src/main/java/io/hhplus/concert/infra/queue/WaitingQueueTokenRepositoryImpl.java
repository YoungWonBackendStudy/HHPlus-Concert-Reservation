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
    private final RedisTemplate<String, String> waitingQueueTokenRedisTemplate;
    private final MyRedisKeyspaceConfig keyspace;

    @Override
    public WaitingQueueToken saveWaitingQueueToken(WaitingQueueToken token) {
        waitingQueueTokenRedisTemplate.opsForZSet().add(keyspace.getWaitingToken(), token.getToken(), token.getIssuedAtInMillis());
        return token;
    }

    @Override
    public Long getWaitingQueueTokenRank(WaitingQueueToken token) {
        var rank =  waitingQueueTokenRedisTemplate.opsForZSet().rank(keyspace.getWaitingToken(), token.getToken());
        if(rank == null) throw new CustomNotFoundException(ExceptionCode.WAITING_TOKEN_NOT_FOUND);

        return rank;
    }

    @Override
    public List<WaitingQueueToken> dequeFirstNWaitingQueueTokens(Long sizeN) {
        var tokens = waitingQueueTokenRedisTemplate.opsForZSet().range(keyspace.getWaitingToken(), 0L, sizeN);
        if(tokens == null || tokens.isEmpty()) return List.of();

        waitingQueueTokenRedisTemplate.opsForZSet().removeRange(keyspace.getWaitingToken(), 0L, sizeN);

        return tokens.stream().map(token -> new WaitingQueueToken(token, null)).toList();
    }

    @Override
    public WaitingQueueToken getWaitingQueueTokenByTokenStr(String tokenStr) {
        var issuedAt = waitingQueueTokenRedisTemplate.opsForZSet().score(keyspace.getWaitingToken(), tokenStr);
        if(issuedAt == null) throw new CustomNotFoundException(ExceptionCode.WAITING_TOKEN_NOT_FOUND);

        return new WaitingQueueToken(tokenStr, issuedAt.longValue());
    }
}