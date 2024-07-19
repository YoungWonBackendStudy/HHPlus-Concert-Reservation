package io.hhplus.concert.domain.concert;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class ConcertService {
    ConcertRepository concertRepository;

    public ConcertService(ConcertRepository concertSeatRepository) {
        this.concertRepository = concertSeatRepository;
    }

    public List<Concert> getConcerts() {
        return concertRepository.getConcerts();
    }

    public List<ConcertSchedule> getConcertSchedules(long concertId) {
        return concertRepository.getConcertSchedulesByScheduleId(concertId);
    }

    public List<ConcertSeat> getConcertSeats(long concertScheduleId) {
        return concertRepository.getConcertSeatsByConcertScheduleId(concertScheduleId);
    }

    @Transactional
    public List<ConcertSeat> getConcertSeatsByIds(List<Long> seatIds) {
        return concertRepository.getAndLockConcertSeatsByIdIn(seatIds);
    }
}