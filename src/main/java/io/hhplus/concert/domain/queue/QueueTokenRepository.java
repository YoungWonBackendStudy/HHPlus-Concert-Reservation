package io.hhplus.concert.domain.queue;

import java.util.Date;
import java.util.List;

import io.hhplus.concert.domain.queue.QueueToken.TokenStatus;

public interface QueueTokenRepository {
    QueueToken saveToken(QueueToken queueToken);
    List<QueueToken> saveAllTokens(List<QueueToken> queueTokens);

    QueueToken getTokenByTokenString(String token);
    
    QueueToken getActiveTokenByUserId(long userId);
    QueueToken getFirstTokenByStatus(TokenStatus tokenStatus);
    List<QueueToken> getTokensByStatus(TokenStatus tokenStatus, int size);

    List<QueueToken> getActiveTokensActivatedAtBefore(Date date);
}
