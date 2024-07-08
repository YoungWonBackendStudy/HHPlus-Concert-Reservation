package io.hhplus.concert.domain.concert;

import org.springframework.stereotype.Service;

@Service
public class ConcertService {
    ConcertScheduleRepository concertScheduleRepository;
    ConcertSeatRepository concertSeatRepository;

    public ConcertService(ConcertScheduleRepository concertScheduleRepository, ConcertSeatRepository concertSeatRepository) {
        this.concertScheduleRepository = concertScheduleRepository;
        this.concertSeatRepository = concertSeatRepository;
    }
}
