package io.hhplus.concert.infra.reservation;

import io.hhplus.concert.domain.reservation.Reservation;
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

    List<ReservationEntity> findByStatusAndReservedAtBefore(Reservation.ReservationStatus status, Date reservedAt);
}
