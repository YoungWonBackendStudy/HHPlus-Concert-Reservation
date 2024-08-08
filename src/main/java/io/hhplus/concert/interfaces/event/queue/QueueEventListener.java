package io.hhplus.concert.interfaces.event.queue;

import io.hhplus.concert.application.queue.QueueFacade;
import io.hhplus.concert.domain.payment.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class QueueEventListener {
    private final QueueFacade queueFacade;

    @Async @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void paymentSuccessEventHandler(PaymentSuccessEvent paymentSuccessEvent) {
        queueFacade.expireActiveToken(paymentSuccessEvent.token());
    }
}
