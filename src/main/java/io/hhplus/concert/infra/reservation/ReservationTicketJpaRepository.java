package io.hhplus.concert.infra.reservation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationTicketJpaRepository extends JpaRepository<ReservationTicketEntity, Long>{
    public List<ReservationTicketEntity> findByReservationId(Long reservationId);
    public List<ReservationTicketEntity> findByConcertScheduleId(Long concertScheduleId);
}
