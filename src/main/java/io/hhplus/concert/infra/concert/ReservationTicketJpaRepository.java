package io.hhplus.concert.infra.concert;

import io.hhplus.concert.infra.concert.entity.ReservationTicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationTicketJpaRepository extends JpaRepository<ReservationTicketEntity, Long>{
    List<ReservationTicketEntity> findByReservationId(Long reservationId);
    @Query(value = """
        SELECT ticket
        FROM ReservationTicketEntity ticket, ReservationEntity reservation
        WHERE (reservation.completedAt is not null OR reservation.reservedAt >= CURRENT_DATE - 5 MINUTE)
            AND ticket.reservationId = reservation.id
            AND ticket.concertSeatId IN :seatIds
    """)
    List<ReservationTicketEntity> findCompletedOrReservedUnder5minBySeatIds(@Param("seatIds") List<Long> seatIds);

    @Query(value = """
        SELECT ticket
        FROM ReservationTicketEntity ticket, ReservationEntity reservation
        WHERE (reservation.completedAt is not null OR reservation.reservedAt >= CURRENT_DATE - 5 MINUTE)
            AND ticket.reservationId = reservation.id
            AND ticket.concertScheduleId = :scheduleId
    """)
    List<ReservationTicketEntity> findCompletedOrReservedUnder5minByConcertScheduleId(@Param("scheduleId")Long concertScheduleId);
}
