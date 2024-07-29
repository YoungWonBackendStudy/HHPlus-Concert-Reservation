package io.hhplus.concert.domain.concert;

import io.hhplus.concert.support.exception.CustomBadRequestException;
import io.hhplus.concert.support.exception.ExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {
    ReservationRepository reservationRepository;
    ReservationRedissonClient reservationRedissonClient;
    ConcertRepository concertRepository;

    public ReservationService (ReservationRepository reservationRepository, ReservationRedissonClient reservationRedissonClient, ConcertRepository concertRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationRedissonClient = reservationRedissonClient;
        this.concertRepository = concertRepository;
    }

    public Reservation lockAndReserveConcertSeats(long userId, List<ConcertSeat> seats) {
        Reservation newReservation = null;
        List<ReservationLock> locks = new ArrayList<>(seats.size());
        try {
            locks = seats.stream().map(seat -> reservationRedissonClient.tryGetConcertSeatLockFor1s(seat.getId())).toList();
            newReservation = this.reserveConcertSeats(userId, seats);
        } finally {
            locks.forEach(lock -> reservationRedissonClient.releaseLock(lock));
        }

        return newReservation;
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
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

    public Reservation getAndLockReservation(long reservationId) {
        return reservationRepository.getAndLockById(reservationId);
    }

    public void completeReservation(Reservation reservation) {
        reservation.completed();
        reservationRepository.saveReservation(reservation);
    }
}
