package io.hhplus.concert.infra.waiting;

import java.util.Date;

import io.hhplus.concert.domain.waiting.WaitingToken;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;

@Entity
@AllArgsConstructor
public class WaitingTokenEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    
    String token;
    long userId;
    Date issuedAt;
    Date deletedAt;

    public WaitingTokenEntity(WaitingToken waitingToken) {
        this.id = waitingToken.getId();
        this.token = waitingToken.getToken();
        this.userId = waitingToken.getUserId();
        this.issuedAt = waitingToken.getIssuedAt();
        this.deletedAt = waitingToken.getDeletedAt();
    }

    public WaitingToken toDomain() {
        return new WaitingToken(this.id, this.token, this.userId, this.issuedAt, this.deletedAt);
    }
}
