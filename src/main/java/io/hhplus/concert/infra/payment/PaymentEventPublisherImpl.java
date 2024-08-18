package io.hhplus.concert.infra.payment;

import io.hhplus.concert.domain.payment.PaymentSuccessMessage;
import io.hhplus.concert.domain.payment.PaymentSuccessEvent;
import io.hhplus.concert.domain.payment.PaymentEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisherImpl implements PaymentEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;
    @Override
    public void publishPaymentSuccessEvent(PaymentSuccessEvent paymentSuccessEvent) {
        applicationEventPublisher.publishEvent(paymentSuccessEvent);
    }

    @Override
    public void publishPaymentSuccessMessageEvent(PaymentSuccessMessage paymentSuccessMessage) {
        applicationEventPublisher.publishEvent(paymentSuccessMessage);
    }
}
