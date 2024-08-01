package io.hhplus.concert.infra.queue;

import io.hhplus.concert.domain.queue.WaitingQueueToken;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


@NoArgsConstructor
@Getter
@RedisHash
public class WaitingQueueTokenEntity {
    @Id
    String token;

    @Indexed
    long userId;

    Long issuedAtInMillis;

    public WaitingQueueTokenEntity(WaitingQueueToken domain) {
        this.token = domain.getToken();
        this.userId = domain.getUserId();
    }

    public WaitingQueueToken toDomain() {
        return new WaitingQueueToken(this.token, this.userId, this.issuedAtInMillis);
    }
}
