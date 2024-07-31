package io.hhplus.concert.infra.reservation;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.lang.NonNull;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long>{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ReservationEntity> findAndLockById(@NonNull Long id);

    List<ReservationEntity> findByReservedAtBefore(Date reservedAt);
}
