package io.hhplus.concert.interfaces.scheduler.payment;

import io.hhplus.concert.application.payment.PaymentFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentOutboxScheduler {
    private final PaymentFacade paymentFacade;
    @Scheduled(fixedDelay = 60000)
    void checkOutboxCompleted() {
        paymentFacade.handleFailedPayment();
    }
}
