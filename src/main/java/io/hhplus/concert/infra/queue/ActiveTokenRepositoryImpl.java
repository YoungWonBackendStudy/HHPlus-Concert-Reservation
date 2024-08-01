package io.hhplus.concert.infra.queue;

import io.hhplus.concert.domain.queue.ActiveToken;
import io.hhplus.concert.domain.queue.ActiveTokenRepository;
import io.hhplus.concert.support.exception.CustomNotFoundException;
import io.hhplus.concert.support.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ActiveTokenRepositoryImpl implements ActiveTokenRepository {
    private final ActiveTokenRedisRepository activeTokenRedisRepository;

    @Override
    public ActiveToken getActiveTokenByTokenString(String tokenString) {
        var activeToken = activeTokenRedisRepository.findById(tokenString).orElse(null);
        if(activeToken == null) throw new CustomNotFoundException(ExceptionCode.ACTIVE_TOKEN_NOT_FOUND);
        return activeToken.toDomain();
    }

    @Override
    public void deleteActiveToken(ActiveToken activeToken) {
        var entity = activeTokenRedisRepository.findById(activeToken.getToken()).orElse(null);
        if(entity == null) throw new CustomNotFoundException(ExceptionCode.ACTIVE_TOKEN_NOT_FOUND);

        activeTokenRedisRepository.delete(entity);
    }

    @Override
    public ActiveToken getActiveTokenByUserId(long userId) {
        var activeToken = activeTokenRedisRepository.findByUserId(userId);
        if(activeToken == null) throw new CustomNotFoundException(ExceptionCode.ACTIVE_TOKEN_NOT_FOUND);
        return activeToken.toDomain();
    }

    @Override
    public void saveActiveTokens(List<ActiveToken> activeTokens) {
        var entities = activeTokens.stream().map(ActiveTokenEntity::new).toList();
        activeTokenRedisRepository.saveAll(entities);
    }

    @Override
    public Long getActiveTokensCount() {
        return activeTokenRedisRepository.count();
    }
}
