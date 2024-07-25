package io.hhplus.concert.domain.concert;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConcertService {
    private final ConcertRepository concertRepository;
    private final ReservationRepository reservationRepository;
    public ConcertService(ConcertRepository concertRepository, ReservationRepository reservationRepository) {
        this.concertRepository = concertRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Concert> getConcerts() {
        return concertRepository.getConcerts();
    }

    public List<ConcertSchedule> getConcertSchedules(long concertId) {
        return concertRepository.getConcertSchedulesByConcertId(concertId);
    }

    public List<ConcertSeat> getConcertSeats(long concertScheduleId) {
        var concertSchedule = concertRepository.getConcertScheduleById(concertScheduleId);
        return concertRepository.getConcertSeatsByConcertPlaceId(concertSchedule.getConcertPlace().getId());
    }

    public List<ConcertSeat> getReservedConcertSeats(long concertScheduleId) {
        var reservedTickets = reservationRepository.getCompletedOrReservedUnder5mins(concertScheduleId);
        var seatIds = reservedTickets.stream().map(ReservationTicket::getConcertSeatId).toList();
        return concertRepository.getAndLockConcertSeatsByIdIn(seatIds);
    }

    @Transactional
    public List<ConcertSeat> getConcertSeatsByIds(List<Long> seatIds) {
        return concertRepository.getAndLockConcertSeatsByIdIn(seatIds);
    }
}