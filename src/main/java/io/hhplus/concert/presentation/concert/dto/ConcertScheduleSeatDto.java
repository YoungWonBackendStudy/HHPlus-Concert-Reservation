package io.hhplus.concert.presentation.concert.dto;

public class ConcertScheduleSeatDto {
    public static record Request(
        String token,
        long concertScheduleId
    ) {
    }

    public static record Response(
        long id,
        String location,
        boolean reserved,
        long price
    ) {
    }
}
