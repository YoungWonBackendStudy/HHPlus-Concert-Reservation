package io.hhplus.concert.domain.concert;

import java.util.List;
public interface ReservationRepository {
    Reservation getAndLockById(Long id);
    Reservation saveReservation(Reservation reservation);
    List<ReservationTicket> saveReservationTickets(List<ReservationTicket> reservationTickets);
    List<ReservationTicket> getReservedTicketsByConcertScheduleId(long concertScheduleId);
}
