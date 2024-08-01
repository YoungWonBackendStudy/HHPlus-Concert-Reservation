package io.hhplus.concert.infra.queue;

import io.hhplus.concert.domain.queue.ActiveToken;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.Date;

@NoArgsConstructor
@RedisHash
public class ActiveTokenEntity {
    @Id
    String token;

    @Indexed
    Long userId;

    @TimeToLive
    Long timeToLive;

    public ActiveTokenEntity(ActiveToken domain) {
        this.token = domain.getToken();
        this.userId = domain.getUserId();
        this.timeToLive = domain.getTimeToLive();
    }

    public ActiveToken toDomain() {
        return new ActiveToken(token, userId, timeToLive);
    }
}
