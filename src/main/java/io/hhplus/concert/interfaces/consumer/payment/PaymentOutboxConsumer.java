package io.hhplus.concert.interfaces.consumer.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.concert.domain.payment.PaymentSuccessMessage;
import io.hhplus.concert.domain.payment.PaymentOutboxRepository;
import io.hhplus.concert.domain.payment.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentOutboxConsumer {
    private final PaymentOutboxRepository paymentOutboxRepository;

    @KafkaListener(topics = "payment-success")
    void completePaymentSuccessOutbox(ConsumerRecord<String, String> record) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        PaymentSuccessMessage paymentSuccessMessage  = mapper.readValue(record.value(), PaymentSuccessMessage.class);
        paymentSuccessMessage.published();
        paymentOutboxRepository.save(paymentSuccessMessage);
    }
}
