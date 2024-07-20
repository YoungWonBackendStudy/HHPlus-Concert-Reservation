package io.hhplus.concert.application.queue;

import org.springframework.stereotype.Component;

import io.hhplus.concert.domain.queue.TokenService;
import io.hhplus.concert.domain.queue.QueueService;
import io.hhplus.concert.domain.queue.QueueToken;

@Component
public class QueueFacade {
    QueueService queueService;
    TokenService tokenService;

    public QueueFacade(QueueService queueService, TokenService tokenGenerateService) {
        this.queueService = queueService;
        this.tokenService = tokenGenerateService;
    }

    public WaitingQueueDto getQueueToken(long userId) {
        QueueToken token = tokenService.getToken(userId);
        long waitingTokensAhead = queueService.getWaitingTokensAhead(token);
        return new WaitingQueueDto(token, waitingTokensAhead);
    }

    public void scheduleWaitingQueue() {
        tokenService.expireTokens();
        queueService.activateTokens();
    }
}
