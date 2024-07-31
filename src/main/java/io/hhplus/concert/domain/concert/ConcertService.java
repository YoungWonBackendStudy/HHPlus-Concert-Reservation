package io.hhplus.concert.domain.concert;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConcertService {
    private final ConcertRepository concertRepository;
    public ConcertService(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    public List<Concert> getConcerts() {
        return concertRepository.getConcerts();
    }

    public List<ConcertSchedule> getConcertSchedules(long concertId) {
        return concertRepository.getConcertSchedulesByConcertId(concertId);
    }

    public List<ConcertSeat> getConcertSeats(long concertScheduleId) {
        return concertRepository.getConcertSeatsByConcertScheduleId(concertScheduleId);
    }

    public List<ConcertSeat> getConcertSeatsByIds(List<Long> seatIds) {
        return concertRepository.getConcertSeatsByIdIn(seatIds);
    }

    public void reserveConcertSeats(List<ConcertSeat> concertSeats) {
        concertSeats.forEach(ConcertSeat::reserved);
        concertRepository.saveConcertSeats(concertSeats);
    }

    public void expireConcertSeats(List<Long> seatIds) {
        var concertSeats = concertRepository.getConcertSeatsByIdIn(seatIds);
        concertSeats.forEach(ConcertSeat::reservationExpired);
        concertRepository.saveConcertSeats(concertSeats);
    }
}