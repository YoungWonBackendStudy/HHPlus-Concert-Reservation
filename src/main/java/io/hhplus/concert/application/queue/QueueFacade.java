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

    public WaitingQueueDto getQueueToken(long userId) {
        WaitingQueueToken token = tokenService.getWaitingQueueToken(userId);
        long waitingTokensAhead = queueService.getWaitingTokensAhead(token);
        return new WaitingQueueDto(token, waitingTokensAhead);
    }

    public void scheduleWaitingQueue() {
        queueService.activateTokens();
    }
}
