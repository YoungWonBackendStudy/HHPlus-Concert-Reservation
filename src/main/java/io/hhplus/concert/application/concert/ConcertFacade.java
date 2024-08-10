package io.hhplus.concert.application.concert;

import io.hhplus.concert.domain.concert.ConcertService;
import io.hhplus.concert.domain.reservation.ReservationService;
import org.springframework.cache.annotation.Cacheable;
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


    @Cacheable(cacheNames = "ConcertFacade.getConcerts", key = "#page")
    public List<ConcertDto> getConcerts(int page) {
        return concertService.getConcerts(page).stream().map(ConcertDto::new).toList();
    }

    public List<ConcertScheduleDto> getConcertSchedules(long concertId) {
        return concertService.getConcertSchedules(concertId).stream().map(ConcertScheduleDto::new).toList();
    }

    public List<ConcertSeatDto> getConcertSeats(long concertScheduleId) {
        return concertService.getConcertSeats(concertScheduleId).stream()
            .map(ConcertSeatDto::new).toList();
    }
}
