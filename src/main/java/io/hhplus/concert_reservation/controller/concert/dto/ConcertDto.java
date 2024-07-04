package io.hhplus.concert_reservation.controller.concert.dto;

public class ConcertDto {
    public static record Response(
        long id,
        String name,
        String description
    ) {
    }
}
