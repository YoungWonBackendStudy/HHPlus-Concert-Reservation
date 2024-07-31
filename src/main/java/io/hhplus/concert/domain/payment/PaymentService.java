package io.hhplus.concert.domain.payment;

import org.springframework.stereotype.Service;

import io.hhplus.concert.domain.reservation.Reservation;
@Service
public class PaymentService {
    PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment placePayment(Reservation reservation) {
        reservation.validatePayable();
        Payment payment = new Payment(reservation);
        return paymentRepository.save(payment);
    }
}
