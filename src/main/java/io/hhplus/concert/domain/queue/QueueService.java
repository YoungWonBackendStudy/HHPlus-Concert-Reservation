package io.hhplus.concert.domain.queue;

import io.hhplus.concert.support.exception.CustomNotFoundException;
import io.hhplus.concert.support.exception.ExceptionCode;
import org.springframework.stereotype.Service;

import io.hhplus.concert.domain.queue.WaitingQueueToken.TokenStatus;

@Service
public class QueueService {
    final int queueSize = 50;

    WaitingQueueTokenRepository waitingQueueTokenRepository;

    public QueueService(WaitingQueueTokenRepository waitingQueueTokenRepository) {
        this.waitingQueueTokenRepository = waitingQueueTokenRepository;
    }

    public Long getWaitingTokensAhead(WaitingQueueToken token) {
        WaitingQueueToken firstWaitingToken;
        try{
            firstWaitingToken = this.waitingQueueTokenRepository.getFirstTokenByStatus(TokenStatus.WAITING);
        }catch(CustomNotFoundException e) {
            if(!e.getCode().equals(ExceptionCode.TOKEN_NOT_FOUND)) throw e;

            firstWaitingToken = token;
        }
        return token.getId() - firstWaitingToken.getId();
    }

    public void activateTokens() {
        var activeTokens = waitingQueueTokenRepository.getTokensByStatus(TokenStatus.ACTIVE, queueSize + 1);
        if (activeTokens.size() >= queueSize)
            return;

        var tokensToActivate = waitingQueueTokenRepository.getTokensByStatus(TokenStatus.WAITING, queueSize - activeTokens.size());
        if (tokensToActivate == null || tokensToActivate.isEmpty())
            return;

        tokensToActivate.forEach(WaitingQueueToken::activate);
        waitingQueueTokenRepository.saveTokens(tokensToActivate);
    }
}
