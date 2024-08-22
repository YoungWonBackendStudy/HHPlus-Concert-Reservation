package io.hhplus.concert.infra.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.concert.domain.payment.PaymentSuccessMessage;
import io.hhplus.concert.domain.payment.PaymentMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentMessageProducerImpl implements PaymentMessageProducer {
    private final KafkaTemplate<String ,String> kafkaTemplate;
    @Override
    public void producePaymentSuccessMessage(PaymentSuccessMessage paymentSuccessEventKafkaMessage) throws JsonProcessingException {
        log.info("Sending payment success message {}", paymentSuccessEventKafkaMessage.getPayload());
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonMessage = objectMapper.writeValueAsString(paymentSuccessEventKafkaMessage);
        log.info("Sending payment success message {}", jsonMessage);
        kafkaTemplate.send("payment-success", jsonMessage);
    }
}
