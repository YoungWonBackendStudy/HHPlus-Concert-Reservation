package io.hhplus.concert.domain.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.hhplus.concert.domain.reservation.Reservation;
import io.hhplus.concert.support.exception.CustomBadRequestException;
import io.hhplus.concert.support.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentEventPublisher paymentEventPublisher;
    private final PaymentOutboxRepository paymentOutboxRepository;

    public Payment placePayment(Reservation reservation) {
        reservation.validatePayable();
        Payment payment = new Payment(reservation);
        return paymentRepository.save(payment);
    }

    @Transactional
    public void paymentCompleted(String token, Payment payment) throws JsonProcessingException {
        var paymentSuccessEvent = new PaymentSuccessEvent(payment, token);
        paymentEventPublisher.publishPaymentSuccessEvent(paymentSuccessEvent);
        var paymentSuccessMessage = new PaymentSuccessMessage(paymentSuccessEvent);
        paymentEventPublisher.publishPaymentSuccessMessageEvent(paymentSuccessMessage);
    }

    @Transactional
    public void handleFailedMessages() {
        var failedPaymentSuccessMessages =  paymentOutboxRepository.getPaymentSuccessMessagesOver3MinsAndStillInit();
        failedPaymentSuccessMessages.forEach(paymentMessage -> {
            try{
                paymentMessage.messageResent();
            } catch(CustomBadRequestException e) {
                if(e.getCode() != ExceptionCode.MESSAGE_RESENT_THRESHOLD) throw e;
                log.error("{} (message Id: {})", e.getMessage(), paymentMessage.getId());
            }
        });
        failedPaymentSuccessMessages.forEach(paymentEventPublisher::publishPaymentSuccessMessageEvent);
    }
}