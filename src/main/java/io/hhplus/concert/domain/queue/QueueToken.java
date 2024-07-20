package io.hhplus.concert.domain.queue;

import java.util.Date;
import java.util.UUID;

import io.hhplus.concert.support.exception.CustomBadRequestException;
import io.hhplus.concert.support.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QueueToken {
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

    public QueueToken(long userId) {
        this.userId = userId;
        this.token = UUID.randomUUID().toString();
        this.issuedAt = new Date();
        this.status = TokenStatus.WAITING;
    }

    public void validateActivation() {
        if(this.status.equals(TokenStatus.EXPIRED)) {
            throw new CustomBadRequestException(ExceptionCode.TOKEN_EXPIRED);
        }

        if(!this.status.equals(TokenStatus.ACTIVE)) {
            throw new CustomBadRequestException(ExceptionCode.TOKEN_NOT_ACTIVATED);
        }
    }

    public void validateWaiting() {
        if(!this.status.equals(TokenStatus.WAITING)) {
            throw new CustomBadRequestException(ExceptionCode.TOKEN_NOT_WAITING);
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