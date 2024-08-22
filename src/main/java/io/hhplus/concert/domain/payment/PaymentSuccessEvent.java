package io.hhplus.concert.domain.payment;

public record PaymentSuccessEvent(
        Payment payment,
        String token
) {
}
