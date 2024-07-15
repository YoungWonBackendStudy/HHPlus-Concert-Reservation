package io.hhplus.concert.interfaces.scheduler.waiting;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.hhplus.concert.application.waiting.WaitingFacade;

@Component
public class WaitingScheduler {
    WaitingFacade waitingFacade;

    public WaitingScheduler(WaitingFacade waitingFacade) {
        this.waitingFacade = waitingFacade;
    }

    @Scheduled(fixedDelay = 1000)
    void scheduleWaitings() {
        waitingFacade.scheduleWaiting();
    }
}
