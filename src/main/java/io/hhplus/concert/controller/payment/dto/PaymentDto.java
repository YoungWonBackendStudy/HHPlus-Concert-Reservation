package io.hhplus.concert.controller.payment.dto;

public class PaymentDto {
    public static record Request(
        long paymentId
    ) {
    }

    public static record Response(
        long paidAmount,
        long balance
    ) {
    }
}
