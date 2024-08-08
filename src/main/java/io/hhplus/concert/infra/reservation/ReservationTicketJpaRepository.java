package io.hhplus.concert.infra.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationTicketJpaRepository extends JpaRepository<ReservationTicketEntity, Long>{
    List<ReservationTicketEntity> findByReservationId(Long reservationId);

    List<ReservationTicketEntity> findByReservationIdIn(List<Long> reservationId);
}
