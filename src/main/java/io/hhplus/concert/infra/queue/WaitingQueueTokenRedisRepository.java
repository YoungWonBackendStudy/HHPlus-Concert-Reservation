package io.hhplus.concert.infra.queue;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaitingQueueTokenRedisRepository extends CrudRepository<WaitingQueueTokenEntity, String> {
    WaitingQueueTokenEntity findByUserId(Long userId);
    List<WaitingQueueTokenEntity> findByTokenIn(List<String> tokens);
}
