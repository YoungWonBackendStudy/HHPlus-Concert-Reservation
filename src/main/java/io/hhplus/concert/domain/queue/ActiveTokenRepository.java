package io.hhplus.concert.domain.queue;

import java.util.List;

public interface ActiveTokenRepository {
    ActiveToken getActiveTokenByTokenString(String tokenString);
    void deleteActiveToken(ActiveToken activeToken);
    ActiveToken getActiveTokenByUserId(long userId);
    void saveActiveTokens(List<ActiveToken> activeTokens);
    Long getActiveTokensCount();
}
