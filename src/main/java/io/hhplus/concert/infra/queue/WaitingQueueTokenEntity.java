package io.hhplus.concert.infra.queue;

import java.util.Date;

import io.hhplus.concert.domain.queue.WaitingQueueToken;
import io.hhplus.concert.domain.queue.WaitingQueueToken.TokenStatus;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity 
@Table(name = "queue_token"
        ,uniqueConstraints = {
                @UniqueConstraint(name = "lectureScheduleUserUnique", columnNames = {"userId", "status"})
        }
)
@NoArgsConstructor
public class WaitingQueueTokenEntity {
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

    public WaitingQueueTokenEntity(WaitingQueueToken domain) {
        this.id = domain.getId();
        this.token = domain.getToken();
        this.status = domain.getStatus();
        this.userId = domain.getUserId();
        this.issuedAt = domain.getIssuedAt();
        this.activatedAt = domain.getActivatedAt();
        this.deletedAt = domain.getDeletedAt();
    }

    public WaitingQueueToken toDomain() {
        return new WaitingQueueToken(this.id, this.token, this.status, this.userId, this.issuedAt, this.activatedAt, this.deletedAt);
    }
}
