package io.hhplus.concert.domain.concert;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    ReservationRepository reservationRepository;

    public ReservationService (ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Reservation reserveConcertSeats(long userId, List<ConcertSeat> seats) {
        var newReservation = new Reservation(userId);
        newReservation = reservationRepository.saveReservation(newReservation);

        newReservation.makeTickets(seats);
        newReservation = reservationRepository.saveReservation(newReservation);

        return newReservation;
    }

    @Transactional
    public Reservation validateAndGetReservation(long reservationId) {
        var reservation = reservationRepository.getAndLockById(reservationId);
        reservation.validatePayable();

        return reservation;
    }

    public void completeReservation(Reservation reservation) {
        reservation.completed();
        reservationRepository.saveReservation(reservation);
    }
}
