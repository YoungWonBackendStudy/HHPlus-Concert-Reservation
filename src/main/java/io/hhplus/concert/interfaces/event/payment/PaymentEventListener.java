package io.hhplus.concert.interfaces.event.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.hhplus.concert.domain.payment.PaymentSuccessMessage;
import io.hhplus.concert.domain.payment.PaymentMessageProducer;
import io.hhplus.concert.domain.payment.PaymentOutboxRepository;
import io.hhplus.concert.domain.payment.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventListener {
    private final PaymentMessageProducer paymentMessageProducer;
    private final PaymentOutboxRepository paymentOutboxRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    void initPaymentSuccessOutbox(PaymentSuccessMessage paymentSuccessMessage) throws JsonProcessingException {
        paymentOutboxRepository.save(paymentSuccessMessage);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void producePaymentSuccessMessage(PaymentSuccessMessage paymentSuccessMessage) throws JsonProcessingException {
        paymentMessageProducer.producePaymentSuccessMessage(paymentSuccessMessage);
    }
}
