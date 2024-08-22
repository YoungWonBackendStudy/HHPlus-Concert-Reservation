package io.hhplus.concert.infra.queue;

import io.hhplus.concert.domain.queue.ActiveToken;
import io.hhplus.concert.domain.queue.ActiveTokenRepository;
import io.hhplus.concert.support.config.MyRedisKeyspaceConfig;
import io.hhplus.concert.support.exception.CustomNotFoundException;
import io.hhplus.concert.support.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class ActiveTokenRepositoryImpl implements ActiveTokenRepository {
    private final RedisTemplate<String, String> activeTokenRedisTemplate;
    private final MyRedisKeyspaceConfig keyspace;

    @Override
    public ActiveToken getActiveTokenByTokenString(String tokenString) {
        var activeTokenKey = keyspace.getActiveToken() + ":" + tokenString;
        if(activeTokenRedisTemplate.hasKey(activeTokenKey) == Boolean.FALSE)
            throw new CustomNotFoundException(ExceptionCode.ACTIVE_TOKEN_NOT_FOUND);

        var tokenTtl  = activeTokenRedisTemplate.getExpire(tokenString, TimeUnit.MILLISECONDS);
        return new ActiveToken(tokenString, tokenTtl);
    }

    @Override
    public void deleteActiveToken(ActiveToken activeToken) {
        var activeTokenKey = keyspace.getActiveToken() + ":" + activeToken.getToken();
        activeTokenRedisTemplate.delete(activeTokenKey);
    }

    @Override
    public void saveActiveTokens(List<ActiveToken> activeTokens) {
        activeTokens.forEach(token -> {
            var activeTokenKey = keyspace.getActiveToken() + ":" + token.getToken();
            activeTokenRedisTemplate.opsForValue().set(activeTokenKey, "", Duration.ofMillis(token.getTimeToLive()));
        });
    }

    @Override
    public Long getActiveTokensCount() {
        var activeTokens = activeTokenRedisTemplate.keys(keyspace.getActiveToken() + ":*");
        if(activeTokens == null) return 0L;

        return (long) activeTokens.size();
    }
}
