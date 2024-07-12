package io.hhplus.concert.facade.payment;

import java.util.Date;

import io.hhplus.concert.domain.payment.Payment;
import lombok.Getter;

@Getter
public class PaymentDto {
    long totalPrice;
    Date paidAt;

    public PaymentDto(Payment payment) {
        this.totalPrice = payment.getPrice();
        this.paidAt = payment.getPaidAt();
    }
}
