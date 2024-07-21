package io.hhplus.concert.interfaces.presentation.concert.dto;

import java.util.List;

public record ReservationRequest(
    long concertScheduleId,
    List<Long> seatIds
) {
}
