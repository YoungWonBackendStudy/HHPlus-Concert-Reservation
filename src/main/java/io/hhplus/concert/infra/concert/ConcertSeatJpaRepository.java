package io.hhplus.concert.infra.concert;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import jakarta.persistence.LockModeType;

import java.util.List;

public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeatEntity, Long>{
    List<ConcertSeatEntity> findByConcertScheduleId(Long concertScheduleId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<ConcertSeatEntity> findAndLockByIdIn(List<Long> ids);
}
