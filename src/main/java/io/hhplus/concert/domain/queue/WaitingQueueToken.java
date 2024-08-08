package io.hhplus.concert.domain.queue;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class WaitingQueueToken {
    String token;
    Long issuedAtInMillis;

    public WaitingQueueToken() {
        this.issuedAtInMillis = System.currentTimeMillis();
        this.token = UUID.randomUUID().toString();
    }
}