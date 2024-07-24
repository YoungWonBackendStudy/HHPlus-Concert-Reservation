package io.hhplus.concert.infra.concert;

import io.hhplus.concert.infra.concert.entity.ConcertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertJpaRepository extends JpaRepository<ConcertEntity, Long>{
    
}
