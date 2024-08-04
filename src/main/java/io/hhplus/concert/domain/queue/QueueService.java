package io.hhplus.concert.domain.queue;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QueueService {
    final Long usersPer10s = 100L;
    final Long threshold = 3000L;

    private final WaitingQueueTokenRepository waitingQueueTokenRepository;
    private final ActiveTokenRepository activeTokenRepository;

    public Long getWaitingTokensAhead(WaitingQueueToken token) {
        return this.waitingQueueTokenRepository.getWaitingQueueTokenRank(token);
    }

    @Transactional
    public void activateTokens() {
        var activeTokenCnt = this.activeTokenRepository.getActiveTokensCount();
        if(activeTokenCnt > threshold - usersPer10s) return;

        var tokensToActivate =this.waitingQueueTokenRepository.dequeFirstNWaitingQueueTokens(100L);

        var activeTokens = tokensToActivate.stream().map(ActiveToken::new).toList();
        activeTokenRepository.saveActiveTokens(activeTokens);
    }
}
