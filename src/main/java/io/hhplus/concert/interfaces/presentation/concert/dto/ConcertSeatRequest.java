package io.hhplus.concert.interfaces.presentation.concert.dto;

public record ConcertSeatRequest(
    String token,
    long concertScheduleId
) {
}
