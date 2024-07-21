package io.hhplus.concert.domain.payment;

import org.springframework.stereotype.Service;

import io.hhplus.concert.domain.concert.Reservation;
import jakarta.transaction.Transactional;

@Service
public class PaymentService {
    PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public Payment placePayment(Reservation reservation) {
        Payment payment = new Payment(reservation);
        return paymentRepository.save(payment);
    }
}
