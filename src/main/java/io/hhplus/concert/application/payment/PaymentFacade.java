package io.hhplus.concert.application.payment;

import org.springframework.stereotype.Component;

import io.hhplus.concert.domain.payment.Payment;
import io.hhplus.concert.domain.payment.PaymentService;
import io.hhplus.concert.domain.reservation.Reservation;
import io.hhplus.concert.domain.reservation.ReservationService;
import io.hhplus.concert.domain.user.UserAssetService;
import io.hhplus.concert.domain.waiting.TokenService;
import io.hhplus.concert.domain.waiting.WaitingService;
import io.hhplus.concert.domain.waiting.WaitingToken;
import jakarta.transaction.Transactional;

@Component
public class PaymentFacade {
    TokenService tokenService;
    WaitingService waitingService;
    ReservationService reservationService;
    PaymentService paymentService;
    UserAssetService userAssetService;
    
    public PaymentFacade(TokenService tokenService,  WaitingService waitingService, ReservationService reservationService, PaymentService paymentService,
            UserAssetService userAssetService) {
        this.tokenService = tokenService;
        this.waitingService = waitingService;
        this.reservationService = reservationService;
        this.paymentService = paymentService;
        this.userAssetService = userAssetService;
    }

    @Transactional
    public PaymentDto placePayment(String token,long reservationId) {
        WaitingToken waitingToken = tokenService.validateAndGetActiveToken(token);
        Reservation reservation = reservationService.validateAndGetReservation(reservationId);

        userAssetService.useUserAsset(waitingToken.getUserId(), reservation.getTotalPrice());
        Payment payment = paymentService.placePayment(reservation);

        tokenService.expireToken(token);
        waitingService.activateWaitings();
        reservationService.completeReservation(reservation);

        return new PaymentDto(payment);
    }
}
