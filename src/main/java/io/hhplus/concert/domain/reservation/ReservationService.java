package io.hhplus.concert.domain.reservation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.hhplus.concert.domain.concert.ConcertSeat;

@Service
public class ReservationService {
    ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation reserveConcertSeats(long userId, long concertScheduleId, List<ConcertSeat> seats) {
        List<ReservationTicket> reservedTickets = reservationRepository.getReservedTicketsByConcertScheduleId(concertScheduleId);
        Set<Long> reservedSeats = reservedTickets.stream().map(ReservationTicket::getConcertSeatId).collect(Collectors.toSet());
        seats.forEach(seat -> {
            if(reservedSeats.contains(seat.getId())) throw new RuntimeException("이미 예약된 좌석입니다.");
        });

        var newReservation = new Reservation(userId, seats);
        return reservationRepository.saveReservation(newReservation);
    }

    public Reservation validateAndGetReservation(long reservationId) {
        var reservation = reservationRepository.getById(reservationId);
        reservation.validate();

        return reservation;
    }

    public List<ReservationTicket> getReservedTickets(long concertScheduleId) {
        return reservationRepository.getReservedTicketsByConcertScheduleId(concertScheduleId);
    }

    public void completeReservation(Reservation reservation) {
        reservation.completed();
    }
}
