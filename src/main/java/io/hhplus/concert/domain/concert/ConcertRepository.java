package io.hhplus.concert.domain.concert;

import java.util.List;

public interface ConcertRepository {
    List<Concert> getConcerts();
    List<ConcertSchedule> getConcertSchedulesByConcertId(long concertId);
    List<ConcertSeat> getConcertSeatsByConcertScheduleId(long concertScheduleId);
    List<ConcertSeat> getAndLockConcertSeatsByIdIn(List<Long> concertSeatIds);
}
