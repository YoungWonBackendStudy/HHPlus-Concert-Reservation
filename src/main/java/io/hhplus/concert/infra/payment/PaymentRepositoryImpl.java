package io.hhplus.concert.infra.payment;

import org.springframework.stereotype.Repository;

import io.hhplus.concert.domain.payment.Payment;
import io.hhplus.concert.domain.payment.PaymentRepository;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository{
    PaymentJpaRepository paymentJpaRepository;

    public PaymentRepositoryImpl(PaymentJpaRepository paymentJpaRepository) {
        this.paymentJpaRepository = paymentJpaRepository;
    }

    @Override
    public Payment save(Payment payment) {
        var entity = new PaymentEntity(payment);
        return this.paymentJpaRepository.save(entity).toDomain();
    }
}
