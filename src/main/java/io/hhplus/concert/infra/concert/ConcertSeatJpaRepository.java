package io.hhplus.concert.infra.concert;

import io.hhplus.concert.infra.concert.entity.ConcertSeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeatEntity, Long>{
    List<ConcertSeatEntity> findByConcertPlaceId(Long concertPlaceId);
    List<ConcertSeatEntity> findByIdIn(List<Long> ids);
}
