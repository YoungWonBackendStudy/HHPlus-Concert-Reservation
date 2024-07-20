package io.hhplus.concert.application.waiting;

import io.hhplus.concert.domain.waiting.WaitingToken;
import lombok.Getter;

@Getter
public class WaitingDto {
    String token;
    long waitingId;
    long waitingAhead;

    public WaitingDto(WaitingToken token, long waitingAhead) {
        this.token = token.getToken();
        this.waitingId = token.getId();
        this.waitingAhead = waitingAhead;
    }
}
