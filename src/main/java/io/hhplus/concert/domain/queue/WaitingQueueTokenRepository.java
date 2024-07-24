package io.hhplus.concert.domain.queue;

import java.util.Date;
import java.util.List;

import io.hhplus.concert.domain.queue.WaitingQueueToken.TokenStatus;

public interface WaitingQueueTokenRepository {
    WaitingQueueToken saveToken(WaitingQueueToken waitingQueueToken);
    List<WaitingQueueToken> saveTokens(List<WaitingQueueToken> waitingQueueTokens);
    WaitingQueueToken getTokenByTokenString(String token);
    
    WaitingQueueToken getActiveTokenByUserId(long userId);
    WaitingQueueToken getFirstTokenByStatus(TokenStatus tokenStatus);
    List<WaitingQueueToken> getTokensByStatus(TokenStatus tokenStatus, int size);

    List<WaitingQueueToken> getActiveTokensActivatedAtBefore(Date date);
}
