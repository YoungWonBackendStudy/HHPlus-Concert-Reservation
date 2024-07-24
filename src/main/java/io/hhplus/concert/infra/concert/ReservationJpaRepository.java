package io.hhplus.concert.infra.concert;

import io.hhplus.concert.infra.concert.entity.ReservationEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long>{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ReservationEntity> findAndLockById(@NonNull Long id);
}
