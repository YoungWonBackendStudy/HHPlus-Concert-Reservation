package io.hhplus.concert.domain.queue;

import java.util.List;

public interface WaitingQueueTokenRepository {
    WaitingQueueToken saveWaitingQueueToken(WaitingQueueToken waitingQueueToken);
    Long getWaitingQueueTokenRank(WaitingQueueToken waitingQueueToken);
    List<WaitingQueueToken> dequeFirstNWaitingQueueTokens(Long sizeN);
    WaitingQueueToken getWaitingQueueTokenByUserId(long userId);
    void deleteWaitingQueueToken(WaitingQueueToken waitingQueueToken);
}
