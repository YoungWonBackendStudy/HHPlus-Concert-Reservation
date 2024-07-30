package io.hhplus.concert.application.concert;

import io.hhplus.concert.domain.concert.ConcertService;
import io.hhplus.concert.domain.reservation.ReservationService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConcertFacade {
    ConcertService concertService;
    ReservationService reservationService;

    public ConcertFacade(ConcertService concertService, ReservationService reservationService) {
        this.concertService = concertService;
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
}
