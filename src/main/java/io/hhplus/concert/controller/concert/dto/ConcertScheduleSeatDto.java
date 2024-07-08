package io.hhplus.concert.controller.concert.dto;

public class ConcertScheduleSeatDto {
    public static record Request(
        String passToken,
        long concertScheduleId
    ) {
    }

    public static record Response(
        long id,
        String location,
        long price
    ) {
    }
}
