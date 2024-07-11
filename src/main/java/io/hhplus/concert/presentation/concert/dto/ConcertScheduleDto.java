package io.hhplus.concert.presentation.concert.dto;

import java.util.Date;

public class ConcertScheduleDto {
    public static record Request(
        String token,
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
