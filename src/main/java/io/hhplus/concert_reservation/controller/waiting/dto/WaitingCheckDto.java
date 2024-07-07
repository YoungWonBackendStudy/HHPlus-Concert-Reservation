package io.hhplus.concert_reservation.controller.waiting.dto;

import java.util.Date;

public class WaitingCheckDto {
    public static record Response(
        boolean result,
        long waitingId,
        long precedingWaiting,
        String passToken,
        Date expireAt
    ) {
    }
}
