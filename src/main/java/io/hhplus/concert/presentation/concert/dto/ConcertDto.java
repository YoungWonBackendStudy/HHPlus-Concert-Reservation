package io.hhplus.concert.presentation.concert.dto;

import io.hhplus.concert.domain.concert.Concert;

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
        public static Response of(Concert domain) {
            return new Response(domain.getId(), domain.getName(), domain.getDescription());
        }
    }
}
