package io.hhplus.concert.domain.payment;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TestPaymentEventListener {
    AtomicInteger successEventCalledCnt = new AtomicInteger(0);
    AtomicInteger successMessageEventCalledCnt = new AtomicInteger(0);

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void testPaymentSuccess(PaymentSuccessEvent event) {
        successEventCalledCnt.incrementAndGet();
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void testPaymentSuccessMessage(PaymentSuccessMessage event) {
        successMessageEventCalledCnt.incrementAndGet();
    }

    public int getSuccessEventCalledCnt() {
        return successEventCalledCnt.get();
    }

    public int getSuccessMessageEventCalledCnt() {
        return successMessageEventCalledCnt.get();
    }
}
