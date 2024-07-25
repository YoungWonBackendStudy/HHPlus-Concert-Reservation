package io.hhplus.concert.application.payment;

import io.hhplus.concert.domain.concert.ReservationService;
import org.springframework.stereotype.Component;

import io.hhplus.concert.domain.payment.Payment;
import io.hhplus.concert.domain.payment.PaymentService;
import io.hhplus.concert.domain.concert.Reservation;
import io.hhplus.concert.domain.user.UserAssetService;
import io.hhplus.concert.domain.queue.TokenService;
import io.hhplus.concert.domain.queue.QueueService;
import io.hhplus.concert.domain.queue.WaitingQueueToken;
import jakarta.transaction.Transactional;

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
    public PaymentDto placePayment(String token,long reservationId) {
        WaitingQueueToken waitingQueueToken = tokenService.validateAndGetActiveToken(token);
        Reservation reservation = reservationService.getReservation(reservationId);

        userAssetService.useUserAsset(waitingQueueToken.getUserId(), reservation.getTotalPrice());
        Payment payment = paymentService.placePayment(reservation);

        tokenService.expireToken(token);
        queueService.activateTokens();
        reservationService.completeReservation(reservation);

        return new PaymentDto(payment);
    }
}
