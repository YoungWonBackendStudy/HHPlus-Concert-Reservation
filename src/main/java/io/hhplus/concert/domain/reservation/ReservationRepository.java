package io.hhplus.concert.domain.reservation;

import java.util.List;
public interface ReservationRepository {
    Reservation getAndLockById(Long id);
    Reservation saveReservation(Reservation reservation);
    List<ReservationTicket> saveReservationTickets(List<ReservationTicket> reservationTickets);
    List<Reservation> getReservedOver5minsAndStatusStillReserved();
}
