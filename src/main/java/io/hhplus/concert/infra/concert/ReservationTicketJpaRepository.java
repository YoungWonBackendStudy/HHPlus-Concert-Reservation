package io.hhplus.concert.infra.concert;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationTicketJpaRepository extends JpaRepository<ReservationTicketEntity, Long>{
    List<ReservationTicketEntity> findByReservationId(Long reservationId);
    List<ReservationTicketEntity> findByConcertScheduleId(Long concertScheduleId);
}
