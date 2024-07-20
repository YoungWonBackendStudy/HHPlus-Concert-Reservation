package io.hhplus.concert.infra.reservation;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.lang.NonNull;

import jakarta.persistence.LockModeType;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long>{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ReservationEntity> findAndLockById(@NonNull Long id);
}
