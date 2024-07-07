package io.hhplus.concert_reservation.controller.concert.dto;

import java.util.Date;

public class ConcertScheduleDto {
    public static record Request(
        String passToken,
        long concertId
    ) {
    }

    public static record Response(
        long id,
        Date date,
        String address
    ) {
    }
}
