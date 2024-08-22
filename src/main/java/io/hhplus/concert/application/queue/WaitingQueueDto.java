package io.hhplus.concert.application.queue;

import io.hhplus.concert.domain.queue.WaitingQueueToken;
import lombok.Getter;

@Getter
public class WaitingQueueDto {
    String token;
    long waitingAhead;

    public WaitingQueueDto(WaitingQueueToken token, long waitingAhead) {
        this.token = token.getToken();
        this.waitingAhead = waitingAhead;
    }
}
