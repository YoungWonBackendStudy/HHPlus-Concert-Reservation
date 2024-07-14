package io.hhplus.concert.domain.concert;

import java.util.List;

public interface ConcertRepository {
    public List<Concert> getConcerts();
    public List<ConcertSchedule> getConcertSchedulesByScheduleId(long concertId);
    public List<ConcertSeat> getConcertSeatsByConcertScheduleId(long concertScheduleId);

    public List<ConcertSeat> getConcertSeatsByIdIn(List<Long> concertSeatIds);
}
