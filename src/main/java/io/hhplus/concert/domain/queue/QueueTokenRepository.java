package io.hhplus.concert.domain.queue;

import java.util.Date;
import java.util.List;

import io.hhplus.concert.domain.queue.QueueToken.TokenStatus;

public interface QueueTokenRepository {
    public QueueToken saveToken(QueueToken queueToken);
    public List<QueueToken> saveAllTokens(List<QueueToken> queueTokens);

    public QueueToken getTokenByTokenString(String token);
    
    public QueueToken getActiveTokenByUserId(long userId);
    public QueueToken getFirstTokenOrderByActivatedAtDesc(TokenStatus tokenStatus);
    public List<QueueToken> getTokensByStatus(TokenStatus tokenStatus, int size);

    public List<QueueToken> getActiveTokensActivatedAtBefore(Date date);
}
