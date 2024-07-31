package io.hhplus.concert.domain.reservation;

import io.hhplus.concert.domain.concert.ConcertRepository;
import io.hhplus.concert.domain.concert.ConcertSeat;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    ReservationRepository reservationRepository;
    ConcertRepository concertRepository;

    public ReservationService (ReservationRepository reservationRepository, ConcertRepository concertRepository) {
        this.reservationRepository = reservationRepository;
        this.concertRepository = concertRepository;
    }


    public Reservation reserveConcertSeats(long userId, List<ConcertSeat> seats) {
        var newReservation = new Reservation(userId);
        newReservation = reservationRepository.saveReservation(newReservation);
        newReservation.makeTickets(seats);
        newReservation = reservationRepository.saveReservation(newReservation);

        return newReservation;
    }

    public Reservation getAndLockReservation(long reservationId) {
        return reservationRepository.getAndLockById(reservationId);
    }

    public void completeReservation(Reservation reservation) {
        reservation.completed();
        reservationRepository.saveReservation(reservation);
    }

    public List<Reservation> getExpiredReservations() {
        return this.reservationRepository.getReservedOver5mins();
    }

}
