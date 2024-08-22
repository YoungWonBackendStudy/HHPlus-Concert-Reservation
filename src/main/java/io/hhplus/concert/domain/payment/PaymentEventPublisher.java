package io.hhplus.concert.domain.payment;

public interface PaymentEventPublisher {
    void publishPaymentSuccessEvent(PaymentSuccessEvent paymentSuccessEvent);
    void publishPaymentSuccessMessageEvent(PaymentSuccessMessage paymentSuccessMessage);
}
