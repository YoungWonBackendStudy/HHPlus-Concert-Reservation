package io.hhplus.concert_reservation.controller.waiting.dto;

public class WaitingTokenDto {
    public static record Response(
        String waitingToken
    ) {
    }
}
