package io.hhplus.concert.interfaces.event.datasend;

import io.hhplus.concert.application.datasend.DatasendFacade;
import io.hhplus.concert.domain.payment.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class DatasendEventListener {
    private final DatasendFacade datasendFacade;
    @Async @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void paymentSuccessEventHandler(PaymentSuccessEvent paymentSuccessEvent) {
        datasendFacade.sendPayment(paymentSuccessEvent.payment());
    }
}
