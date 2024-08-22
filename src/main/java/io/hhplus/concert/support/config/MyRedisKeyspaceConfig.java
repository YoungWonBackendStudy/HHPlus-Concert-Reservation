package io.hhplus.concert.support.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;

import java.util.List;

@Getter
public class MyRedisKeyspaceConfig extends KeyspaceConfiguration {
    @Value("${spring.data.redis.keyspace.waiting-token}")
    String waitingToken;

    @Value("${spring.data.redis.keyspace.active-token}")
    String activeToken;

    @Value("${spring.data.redis.keyspace.reservation}")
    String reservation;

    @Override
    public Iterable<KeyspaceConfiguration.KeyspaceSettings> initialConfiguration() {
        return List.of();
    }
}
