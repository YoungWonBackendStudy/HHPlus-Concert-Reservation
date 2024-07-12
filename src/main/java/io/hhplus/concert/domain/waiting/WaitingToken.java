package io.hhplus.concert.domain.waiting;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WaitingToken {
    public static enum TokenStatus {
        WAITING, ACTIVE, EXPIRED
    }

    Long id;
    String token;
    TokenStatus status;
    long userId;
    Date issuedAt;
    Date activatedAt;
    Date deletedAt;

    public WaitingToken(long userId) {
        this.userId = userId;
        this.token = UUID.randomUUID().toString();
        this.issuedAt = new Date();
        this.status = TokenStatus.WAITING;
    }

    public void validateActivation() {
        if(!this.status.equals(TokenStatus.ACTIVE)) {
            throw new RuntimeException("토큰이 활성화 상태가 아닙니다.");
        }
    }

    public void activate() {
        this.status = TokenStatus.ACTIVE;
        this.activatedAt = new Date();
    }
    
    public void expire() {
        this.status = TokenStatus.EXPIRED;
        this.deletedAt = new Date();
    }
}