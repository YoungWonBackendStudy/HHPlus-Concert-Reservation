package io.hhplus.concert.presentation.payment.dto;

public class PaymentDto {
    public static record Request(
        String token,
        long reservationId
    ) {
    }

    public static record Response(
        long paidAmount,
        long balance
    ) {
    }
}
