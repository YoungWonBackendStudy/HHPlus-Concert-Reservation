package io.hhplus.concert.interfaces.presentation.payment.dto;

import java.util.Date;

import io.hhplus.concert.application.payment.PaymentDto;

public record PaymentResponse(
    long paidAmount,
    Date paidAt
) {
    public static PaymentResponse of(PaymentDto facadeDto) {
        return new PaymentResponse(facadeDto.getTotalPrice(), facadeDto.getPaidAt());
    }
}
