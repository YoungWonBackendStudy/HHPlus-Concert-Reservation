package io.hhplus.concert.application.queue;

import org.springframework.stereotype.Component;

import io.hhplus.concert.domain.queue.TokenService;
import io.hhplus.concert.domain.queue.QueueService;
import io.hhplus.concert.domain.queue.WaitingQueueToken;

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

    public void scheduleWaitingQueue() {
        queueService.activateTokens();
    }

    public void expireActiveToken(String token) {
        tokenService.expireToken(token);
    }
}
