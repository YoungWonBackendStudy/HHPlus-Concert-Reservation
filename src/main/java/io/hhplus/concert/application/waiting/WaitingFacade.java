package io.hhplus.concert.application.waiting;

import org.springframework.stereotype.Component;

import io.hhplus.concert.domain.waiting.TokenService;
import io.hhplus.concert.domain.waiting.WaitingService;
import io.hhplus.concert.domain.waiting.WaitingToken;

@Component
public class WaitingFacade {
    WaitingService waitingService;
    TokenService tokenService;

    public WaitingFacade(WaitingService waitingService, TokenService tokenGenerateService) {
        this.waitingService = waitingService;
        this.tokenService = tokenGenerateService;
    }

    public WaitingDto getWaitingToken(long userId) {
        WaitingToken token = tokenService.getToken(userId);
        long getWaitingsAhead = waitingService.getWaitingsAhead(token);
        return new WaitingDto(token, getWaitingsAhead);
    }

    public void scheduleWaiting() {
        tokenService.expireTokens();
        waitingService.activateWaitings();
    }
}
