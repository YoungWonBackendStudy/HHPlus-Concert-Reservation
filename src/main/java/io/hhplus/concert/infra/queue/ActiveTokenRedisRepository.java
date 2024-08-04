package io.hhplus.concert.infra.queue;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActiveTokenRedisRepository extends CrudRepository<ActiveTokenEntity, String> {
    ActiveTokenEntity findByUserId(Long userId);
}
