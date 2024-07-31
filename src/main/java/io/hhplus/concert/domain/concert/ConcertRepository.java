package io.hhplus.concert.domain.concert;

import java.util.List;

public interface ConcertRepository {
    List<Concert> getConcerts();
    List<ConcertSchedule> getConcertSchedulesByConcertId(long concertId);
    ConcertSchedule getConcertScheduleById(long concertScheduleId);
    List<ConcertSeat> getConcertSeatsByConcertScheduleId(long concertPlaceId);
    List<ConcertSeat> getConcertSeatsByIdIn(List<Long> concertSeatIds);
    List<ConcertSeat> saveConcertSeats(List<ConcertSeat> concertSeats);
}
