package io.hhplus.concert.presentation.concert.dto;

public class ConcertDto {
    public static record Request(
        String token
    ) {
    }

    public static record Response(
        long id,
        String name,
        String description
    ) {
    }
}
