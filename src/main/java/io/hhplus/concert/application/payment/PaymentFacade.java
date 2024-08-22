package io.hhplus.concert.application.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.hhplus.concert.domain.payment.Payment;
import io.hhplus.concert.domain.payment.PaymentService;
import io.hhplus.concert.domain.reservation.Reservation;
import io.hhplus.concert.domain.reservation.ReservationService;
import io.hhplus.concert.domain.user.UserAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentFacade {
    private final ReservationService reservationService;
    private final PaymentService paymentService;
    private final UserAssetService userAssetService;


    @Transactional
    public PaymentDto placePayment(String token, long userId, long reservationId) throws JsonProcessingException {
        Reservation reservation = reservationService.getAndLockReservation(reservationId);

        userAssetService.useUserAsset(userId, reservation.getTotalPrice());
        Payment payment = paymentService.placePayment(reservation);
        reservationService.completeReservation(reservation);
        paymentService.paymentCompleted(token, payment);

        return new PaymentDto(payment);
    }

    public void handleFailedPayment() {
        paymentService.handleFailedMessages();
    }
}
