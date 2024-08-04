package io.hhplus.concert.domain.queue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActiveToken {
    private static final Long defaultTimeToLive = 5 * 60 * 1000L;
    private String token;
    private Long userId;
    private Long timeToLive;

    public ActiveToken(WaitingQueueToken waitingQueueToken) {
        this.token = waitingQueueToken.getToken();
        this.userId = waitingQueueToken.getUserId();
        this.timeToLive = defaultTimeToLive;
    }

    public void renew() {
        this.timeToLive = defaultTimeToLive;
    }
    
}
