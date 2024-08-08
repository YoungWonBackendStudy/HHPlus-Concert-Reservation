package io.hhplus.concert.infra.concert;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeatEntity, Long>{
    List<ConcertSeatEntity> findByConcertScheduleId(Long concertScheduleId);
    List<ConcertSeatEntity> findByIdIn(List<Long> ids);
}
