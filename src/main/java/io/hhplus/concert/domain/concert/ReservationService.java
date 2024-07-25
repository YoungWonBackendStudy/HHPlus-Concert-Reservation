package io.hhplus.concert.domain.concert;

import io.hhplus.concert.support.exception.CustomBadRequestException;
import io.hhplus.concert.support.exception.ExceptionCode;
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
        var seatIds = seats.stream().map(ConcertSeat::getId).toList();
        var reservationTickets = reservationRepository.getCompletedOrReservedUnder5mins(seatIds);
        if(!reservationTickets.isEmpty()) throw new CustomBadRequestException(ExceptionCode.SEAT_ALREADY_RESERVED);

        var newReservation = new Reservation(userId);
        newReservation = reservationRepository.saveReservation(newReservation);
        newReservation.makeTickets(seats);
        newReservation = reservationRepository.saveReservation(newReservation);

        return newReservation;
    }

    @Transactional
    public Reservation getReservation(long reservationId) {
        return reservationRepository.getAndLockById(reservationId);
    }

    public void completeReservation(Reservation reservation) {
        reservation.completed();
        reservationRepository.saveReservation(reservation);
    }
}
