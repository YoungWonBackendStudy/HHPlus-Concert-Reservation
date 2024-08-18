package io.hhplus.concert.infra.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.hhplus.concert.domain.payment.PaymentMessageStatus;
import io.hhplus.concert.domain.payment.PaymentSuccessMessage;
import io.hhplus.concert.domain.payment.PaymentOutboxRepository;
import io.hhplus.concert.domain.payment.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PaymentOutboxRepositoryImpl implements PaymentOutboxRepository {
    private final PaymentSuccessOutboxJpaRepository paymentSuccessOutboxJpaRepository;
    @Override
    public void save(PaymentSuccessMessage paymentSuccessMessage) throws JsonProcessingException {
        paymentSuccessOutboxJpaRepository.save(new PaymentSuccessOutboxEntity(paymentSuccessMessage));
    }

    @Override
    public List<PaymentSuccessMessage> getPaymentSuccessMessagesOver3MinsAndStillInit() {
        Date date5MinsBefore = new Date(System.currentTimeMillis() - 3 * 60 * 1000L);
        return paymentSuccessOutboxJpaRepository.findByProducedAtBeforeAndStatus(date5MinsBefore, PaymentMessageStatus.INIT).stream().map(paymentSuccessOutboxEntity -> {
                PaymentSuccessMessage domain = null;
                try{
                    domain = paymentSuccessOutboxEntity.toDomain();
                } catch(JsonProcessingException e) {
                    log.error("Failed to convert payment message to domain", e);
                }
                return domain;
            }).filter(Objects::nonNull).toList();
    }
}
