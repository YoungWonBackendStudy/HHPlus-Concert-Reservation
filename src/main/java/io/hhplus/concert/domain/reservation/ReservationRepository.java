package io.hhplus.concert.domain.reservation;

import java.util.List;

public interface ReservationRepository {
    Reservation saveReservation(Reservation reservation);
    List<ReservationTicket> saveReservationTickets(List<ReservationTicket> reservationTickets);

    Reservation getAndLockById(long reservationId);
    List<ReservationTicket> getReservedTicketsByConcertScheduleId(long concertScheduleId);
}
