package io.hhplus.concert.infra.queue;

import java.util.Date;

import io.hhplus.concert.domain.queue.QueueToken;
import io.hhplus.concert.domain.queue.QueueToken.TokenStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity 
@Table(name = "queue_token")
@NoArgsConstructor
public class QueueTokenEntity {
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

    public QueueTokenEntity(QueueToken domain) {
        this.id = domain.getId();
        this.token = domain.getToken();
        this.status = domain.getStatus();
        this.userId = domain.getUserId();
        this.issuedAt = domain.getIssuedAt();
        this.activatedAt = domain.getActivatedAt();
        this.deletedAt = domain.getDeletedAt();
    }

    public QueueToken toDomain() {
        return new QueueToken(this.id, this.token, this.status, this.userId, this.issuedAt, this.activatedAt, this.deletedAt);
    }
}
