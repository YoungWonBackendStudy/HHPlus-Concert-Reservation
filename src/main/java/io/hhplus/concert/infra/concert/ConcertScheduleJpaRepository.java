package io.hhplus.concert.infra.concert;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertScheduleEntity, Long>{
    List<ConcertScheduleEntity> findByConcertId(Long concertId);
}
