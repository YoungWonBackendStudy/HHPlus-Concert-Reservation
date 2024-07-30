package io.hhplus.concert.domain.reservation;

import java.util.List;
public interface ReservationRepository {
    Reservation getAndLockById(Long id);
    Reservation saveReservation(Reservation reservation);
    List<ReservationTicket> saveReservationTickets(List<ReservationTicket> reservationTickets);
    List<ReservationTicket> getCompletedOrReservedUnder5mins(List<Long> seadIds);
    List<ReservationTicket> getCompletedOrReservedUnder5mins(Long concertScheduleId);
    List<Reservation> getReservedOver5mins();
}
