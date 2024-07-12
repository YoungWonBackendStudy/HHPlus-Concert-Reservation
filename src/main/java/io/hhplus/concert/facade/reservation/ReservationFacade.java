package io.hhplus.concert.facade.reservation;

import java.util.List;

import org.springframework.stereotype.Component;

import io.hhplus.concert.domain.concert.ConcertSeat;
import io.hhplus.concert.domain.concert.ConcertService;
import io.hhplus.concert.domain.reservation.Reservation;
import io.hhplus.concert.domain.reservation.ReservationService;
import io.hhplus.concert.domain.reservation.ReservationTicket;
import io.hhplus.concert.domain.waiting.TokenService;
import io.hhplus.concert.domain.waiting.WaitingToken;

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

    public ReservationDto reserveSeats(String token, long concertScheduleId, List<Long> seatIds) {
        List<ConcertSeat> concertSeats = concertService.getConcertSeatsByIds(seatIds);
        WaitingToken waitingToken = tokenService.validateAndGetActiveToken(token);

        Reservation reservation = reservationService.reserveConcertSeats(waitingToken.getUserId(), concertScheduleId, concertSeats);

        return new ReservationDto(reservation);
    }
}
