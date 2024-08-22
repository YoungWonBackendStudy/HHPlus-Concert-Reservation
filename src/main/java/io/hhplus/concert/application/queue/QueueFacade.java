package io.hhplus.concert.application.queue;

import io.hhplus.concert.domain.queue.ActiveToken;
import io.hhplus.concert.domain.queue.QueueService;
import io.hhplus.concert.domain.queue.TokenService;
import io.hhplus.concert.domain.queue.WaitingQueueToken;
import org.springframework.stereotype.Component;

@Component
public class QueueFacade {
    QueueService queueService;
    TokenService tokenService;

    public QueueFacade(QueueService queueService, TokenService tokenGenerateService) {
        this.queueService = queueService;
        this.tokenService = tokenGenerateService;
    }

    public WaitingQueueDto getQueueToken(String tokenStr) {
        WaitingQueueToken token = tokenService.getWaitingQueueToken(tokenStr);
        long waitingTokensAhead = queueService.getWaitingTokensAhead(token);
        return new WaitingQueueDto(token, waitingTokensAhead);
    }

    public ActiveToken getActiveToken(String tokenStr) {
        return tokenService.getActiveToken(tokenStr);
    }

    public void scheduleWaitingQueue() {
        queueService.activateTokens();
    }

    public void expireActiveToken(String token) {
        tokenService.expireToken(token);
    }
}
