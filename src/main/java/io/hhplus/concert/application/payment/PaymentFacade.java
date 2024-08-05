package io.hhplus.concert.application.payment;

import io.hhplus.concert.domain.payment.Payment;
import io.hhplus.concert.domain.payment.PaymentService;
import io.hhplus.concert.domain.queue.QueueService;
import io.hhplus.concert.domain.queue.TokenService;
import io.hhplus.concert.domain.reservation.Reservation;
import io.hhplus.concert.domain.reservation.ReservationService;
import io.hhplus.concert.domain.user.UserAssetService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PaymentFacade {
    TokenService tokenService;
    QueueService queueService;
    ReservationService reservationService;
    PaymentService paymentService;
    UserAssetService userAssetService;
    
    public PaymentFacade(TokenService tokenService, QueueService queueService, ReservationService reservationService, PaymentService paymentService,
                         UserAssetService userAssetService) {
        this.tokenService = tokenService;
        this.queueService = queueService;
        this.reservationService = reservationService;
        this.paymentService = paymentService;
        this.userAssetService = userAssetService;
    }

    @Transactional
    public PaymentDto placePayment(String token, long userId, long reservationId) {
        Reservation reservation = reservationService.getAndLockReservation(reservationId);

        userAssetService.useUserAsset(userId, reservation.getTotalPrice());
        Payment payment = paymentService.placePayment(reservation);

        tokenService.expireToken(token);
        reservationService.completeReservation(reservation);

        return new PaymentDto(payment);
    }
}
