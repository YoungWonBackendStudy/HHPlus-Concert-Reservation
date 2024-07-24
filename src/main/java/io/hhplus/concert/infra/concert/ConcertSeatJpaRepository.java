package io.hhplus.concert.infra.concert;

import io.hhplus.concert.infra.concert.entity.ConcertSeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import jakarta.persistence.LockModeType;

import java.util.List;

public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeatEntity, Long>{
    List<ConcertSeatEntity> findByConcertPlaceId(Long concertPlaceId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<ConcertSeatEntity> findAndLockByIdIn(List<Long> ids);
}
