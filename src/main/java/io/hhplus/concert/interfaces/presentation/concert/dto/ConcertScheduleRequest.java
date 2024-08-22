package io.hhplus.concert.interfaces.presentation.concert.dto;

public record ConcertScheduleRequest(
    String token,
    long concertId
) {
}
