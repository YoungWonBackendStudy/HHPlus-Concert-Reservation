package io.hhplus.concert.interfaces.scheduler.queue;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.hhplus.concert.application.queue.QueueFacade;

@Component
public class QueueScheduler {
    QueueFacade queueFacade;

    public QueueScheduler(QueueFacade queueFacade) {
        this.queueFacade = queueFacade;
    }

    @Scheduled(fixedDelay = 10000)
    void scheduleQueue() {
        queueFacade.scheduleWaitingQueue();
    }
}
