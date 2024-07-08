package io.hhplus.concert.domain.waiting;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class WaitingToken {
    Long id;
    String token;
    long userId;
    Date issuedAt;
    Date deletedAt;

    public WaitingToken(long userId) {
        this.userId = userId;
        this.token = UUID.randomUUID().toString();
        this.issuedAt = new Date();
    }
}