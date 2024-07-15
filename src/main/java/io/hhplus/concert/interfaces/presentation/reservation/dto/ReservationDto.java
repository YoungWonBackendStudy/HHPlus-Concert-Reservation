package io.hhplus.concert.interfaces.presentation.reservation.dto;

import java.util.Date;
import java.util.List;

public class ReservationDto {
    public static record Request(
        List<Long> seatIds
    ) {
    }

    public static record Response(
        long reservationId,
        long paymentId,
        long totalPrice,
        Date reservedAt,
        Date expireAt,
        List<ReservedSeatInfo> reservedSeats
    ) {

    }

    public static record ReservedSeatInfo(
        long id,
        String location,
        long price
    ) {

    }
}
