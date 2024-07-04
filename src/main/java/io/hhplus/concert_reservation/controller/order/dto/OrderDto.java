package io.hhplus.concert_reservation.controller.order.dto;

import java.util.Date;
import java.util.List;

public class OrderDto {
    public static record Request(
        List<Long> seatIds
    ) {
    }
    public static record Response(
        long orderId,
        long totalPrice,
        Date orderedAt,
        Date expireAt,
        List<OrderedSeatInfo> orderedSeats
    ) {

    }

    public static record OrderedSeatInfo(
        long id,
        String location,
        long price
    ) {

    }
}
