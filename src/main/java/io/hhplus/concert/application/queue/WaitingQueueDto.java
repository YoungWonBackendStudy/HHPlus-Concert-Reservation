package io.hhplus.concert.application.queue;

import io.hhplus.concert.domain.queue.QueueToken;
import lombok.Getter;

@Getter
public class WaitingQueueDto {
    String token;
    long waitingId;
    long waitingAhead;

    public WaitingQueueDto(QueueToken token, long waitingAhead) {
        this.token = token.getToken();
        this.waitingId = token.getId();
        this.waitingAhead = waitingAhead;
    }
}
