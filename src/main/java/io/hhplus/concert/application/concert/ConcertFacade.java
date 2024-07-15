package io.hhplus.concert.application.concert;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import io.hhplus.concert.domain.concert.ConcertService;
import io.hhplus.concert.domain.reservation.ReservationService;
import io.hhplus.concert.domain.reservation.ReservationTicket;
import io.hhplus.concert.domain.waiting.TokenService;

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

    public List<ConcertDto> getConcerts(String token) {
        tokenService.validateAndGetActiveToken(token);

        return concertService.getConcerts().stream().map(ConcertDto::new).toList();
    }

    public List<ConcertScheduleDto> getConcertSchedules(String token, long concertId) {
        tokenService.validateAndGetActiveToken(token);

        return concertService.getConcertSchedules(concertId)
            .stream().map(ConcertScheduleDto::new).toList();
    }

    public List<ConcertSeatDto> getConcertSeats(String token, long concertScheduleId) {
        tokenService.validateAndGetActiveToken(token);
        List<ReservationTicket> tickets = reservationService.getReservedTickets(concertScheduleId);
        Set<Long> reservedSeatIds = tickets.stream().map(ReservationTicket::getConcertSeatId).collect(Collectors.toSet());
        
        return concertService.getConcertSeats(concertScheduleId).stream()
            .map(seat -> {
               return new ConcertSeatDto(seat, reservedSeatIds.contains(seat.getId())); 
            }).toList();
    }
}
