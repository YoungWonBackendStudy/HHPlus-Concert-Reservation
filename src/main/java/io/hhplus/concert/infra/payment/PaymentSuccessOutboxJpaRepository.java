package io.hhplus.concert.infra.payment;

import io.hhplus.concert.domain.payment.PaymentMessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PaymentSuccessOutboxJpaRepository extends JpaRepository<PaymentSuccessOutboxEntity, String> {
    List<PaymentSuccessOutboxEntity> findByProducedAtBeforeAndStatus(Date producedAt, PaymentMessageStatus status);
    PaymentSuccessOutboxEntity findByPaymentId(Long paymentId);
}
