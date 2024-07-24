package io.hhplus.concert.domain.concert;

import java.util.List;

public interface ConcertRepository {
    List<Concert> getConcerts();
    List<ConcertSchedule> getConcertSchedulesByConcertId(long concertId);
    ConcertSchedule getConcertScheduleById(long concertScheduleId);
    List<ConcertSeat> getConcertSeatsByConcertPlaceId(long concertPlaceId);
    List<ConcertSeat> getAndLockConcertSeatsByIdIn(List<Long> concertSeatIds);
}
