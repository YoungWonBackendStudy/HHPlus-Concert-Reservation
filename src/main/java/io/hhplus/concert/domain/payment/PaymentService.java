package io.hhplus.concert.domain.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import io.hhplus.concert.domain.reservation.Reservation;
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentEventPublisher paymentEventPublisher;

    public Payment placePayment(Reservation reservation) {
        reservation.validatePayable();
        Payment payment = new Payment(reservation);
        return paymentRepository.save(payment);
    }

    public void paymentCompleted(String token, Payment payment) {
        var paymentSuccessEvent = new PaymentSuccessEvent(payment, token);
        paymentEventPublisher.publishPaymentSuccessEvent(paymentSuccessEvent);
    }
}
