package io.hhplus.concert.infra.reservation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import jakarta.persistence.LockModeType;

public interface ReservationTicketJpaRepository extends JpaRepository<ReservationTicketEntity, Long>{
    public List<ReservationTicketEntity> findByReservationId(Long reservationId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public List<ReservationTicketEntity> findByConcertScheduleId(Long concertScheduleId);
}
