package io.hhplus.concert.domain.payment;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface PaymentOutboxRepository {
    void save(PaymentSuccessMessage paymentSuccessMessage) throws JsonProcessingException;
    List<PaymentSuccessMessage> getPaymentSuccessMessagesOver3MinsAndStillInit();
}
