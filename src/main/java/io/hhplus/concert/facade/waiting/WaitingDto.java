package io.hhplus.concert.facade.waiting;

import io.hhplus.concert.domain.waiting.WaitingToken;
import lombok.Getter;

@Getter
public class WaitingDto {
    String token;
    String status;
    long waitingId;
    long waitingAhead;

    public WaitingDto(WaitingToken token, long waitingAhead) {
        this.token = token.getToken();
        this.status = token.getStatus().name();
        this.waitingId = token.getId();
        this.waitingAhead = waitingAhead;
    }
}
