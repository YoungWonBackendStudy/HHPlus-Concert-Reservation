package io.hhplus.concert.application.payment;

import java.util.Date;

import io.hhplus.concert.domain.payment.Payment;
import lombok.Getter;

@Getter
public class PaymentDto {
    Long id;
    long totalPrice;
    Date paidAt;

    public PaymentDto(Payment payment) {
        this.id = payment.getId();
        this.totalPrice = payment.getPrice();
        this.paidAt = payment.getPaidAt();
    }
}
