package io.hhplus.concert.infra.waiting;

import java.util.Date;

import io.hhplus.concert.domain.waiting.WaitingToken;
import io.hhplus.concert.domain.waiting.WaitingToken.TokenStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity 
@Table(name = "waiting_token")
@NoArgsConstructor
public class WaitingTokenEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    
    @Column(name="token", unique = true)
    String token;
    
    @Column(name = "status")
    TokenStatus status;

    @Column(name = "userId")
    long userId;

    @Column(name = "issued_at")
    Date issuedAt;

    @Column(name = "activated_at")
    Date activatedAt;

    @Column(name = "deleted_at")
    Date deletedAt;

    public WaitingTokenEntity(WaitingToken domain) {
        this.id = domain.getId();
        this.token = domain.getToken();
        this.status = domain.getStatus();
        this.userId = domain.getUserId();
        this.issuedAt = domain.getIssuedAt();
        this.activatedAt = domain.getActivatedAt();
        this.deletedAt = domain.getDeletedAt();
    }

    public WaitingToken toDomain() {
        return new WaitingToken(this.id, this.token, this.status, this.userId, this.issuedAt, this.activatedAt, this.deletedAt);
    }
}
