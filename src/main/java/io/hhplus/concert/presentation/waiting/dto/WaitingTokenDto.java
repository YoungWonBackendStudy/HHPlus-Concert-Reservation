package io.hhplus.concert.presentation.waiting.dto;

public class WaitingTokenDto {
    public static record Response(
        String waitingToken,
        long waitingId,
        long waitingAhead
    ) {
    }
}
