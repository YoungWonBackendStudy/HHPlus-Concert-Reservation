package io.hhplus.concert.domain.reservation;

import java.util.List;

public interface ReservationRepository {
    Reservation saveReservation(Reservation reservation);
    Reservation getById(long reservationId);
    List<ReservationTicket> getReservedTicketsByConcertScheduleId(long concertScheduleId);
}
