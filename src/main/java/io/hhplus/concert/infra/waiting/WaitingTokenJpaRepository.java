package io.hhplus.concert.infra.waiting;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingTokenJpaRepository extends JpaRepository<WaitingTokenEntity, Long>{

    
}
