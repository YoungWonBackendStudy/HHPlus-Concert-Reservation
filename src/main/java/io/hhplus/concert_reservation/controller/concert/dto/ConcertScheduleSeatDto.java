package io.hhplus.concert_reservation.controller.concert.dto;

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
