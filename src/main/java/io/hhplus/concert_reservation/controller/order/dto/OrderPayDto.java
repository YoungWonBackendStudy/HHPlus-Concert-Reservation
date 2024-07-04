package io.hhplus.concert_reservation.controller.order.dto;

public class OrderPayDto {
    public static record Request(
        long orderId
    ) {
    }

    public static record Response(
        long paidAmount,
        long balance
    ) {
        
    }
}
