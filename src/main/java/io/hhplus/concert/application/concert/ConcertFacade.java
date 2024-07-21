package io.hhplus.concert.application.concert;

import io.hhplus.concert.domain.concert.ConcertSeat;
import io.hhplus.concert.domain.concert.ConcertService;
import io.hhplus.concert.domain.concert.Reservation;
import io.hhplus.concert.domain.concert.ReservationService;
import io.hhplus.concert.domain.queue.TokenService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConcertFacade {
    ConcertService concertService;
    TokenService tokenService;
    ReservationService reservationService;

    public ConcertFacade(ConcertService concertService, TokenService tokenService, ReservationService reservationService) {
        this.concertService = concertService;
        this.tokenService = tokenService;
        this.reservationService = reservationService;
    }

    public List<ConcertDto> getConcerts() {
        return concertService.getConcerts().stream().map(ConcertDto::new).toList();
    }

    public List<ConcertScheduleDto> getConcertSchedules(long concertId) {
        return concertService.getConcertSchedules(concertId)
            .stream().map(ConcertScheduleDto::new).toList();
    }

    public List<ConcertSeatDto> getConcertSeats(long concertScheduleId) {
        return concertService.getConcertSeats(concertScheduleId).stream()
            .map(ConcertSeatDto::new).toList();
    }

    @Transactional
    public ReservationDto reserveSeats(long userId, List<Long> seatIds) {
        List<ConcertSeat> concertSeats = concertService.getConcertSeatsByIds(seatIds);
        Reservation reservation = reservationService.reserveConcertSeats(userId, concertSeats);
        return new ReservationDto(reservation);
    }
}
