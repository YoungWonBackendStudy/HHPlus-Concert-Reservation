package io.hhplus.concert.domain.payment;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface PaymentMessageProducer {
    void producePaymentSuccessMessage(PaymentSuccessMessage paymentSuccessEventKafkaMessage) throws JsonProcessingException;
}
