package io.hhplus.concert.application.reservation;

import java.util.List;

import org.springframework.stereotype.Component;

import io.hhplus.concert.domain.concert.ConcertSeat;
import io.hhplus.concert.domain.concert.ConcertService;
import io.hhplus.concert.domain.reservation.Reservation;
import io.hhplus.concert.domain.reservation.ReservationService;
import io.hhplus.concert.domain.waiting.TokenService;

@Component
public class ReservationFacade {
    ReservationService reservationService;
    ConcertService concertService;
    TokenService tokenService;

    public ReservationFacade(ReservationService reservationService, ConcertService concertService, TokenService tokenService) {
        this.reservationService = reservationService;
        this.concertService = concertService;
        this.tokenService = tokenService;
    }

    public ReservationDto reserveSeats(long userId, long concertScheduleId, List<Long> seatIds) {
        List<ConcertSeat> concertSeats = concertService.getConcertSeatsByIds(seatIds);
        Reservation reservation = reservationService.reserveConcertSeats(userId, concertScheduleId, concertSeats);

        return new ReservationDto(reservation);
    }
}
