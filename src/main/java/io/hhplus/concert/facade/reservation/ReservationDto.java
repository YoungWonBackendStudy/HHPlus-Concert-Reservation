package io.hhplus.concert.facade.reservation;

import java.util.Date;

import io.hhplus.concert.domain.reservation.Reservation;
import lombok.Getter;

@Getter
public class ReservationDto {
    long reservationId;
    long paymentId;
    long totalPrice;
    Date reservedAt;
    Date expireAt;

    public ReservationDto(Reservation reservation) {
        this.reservationId = reservation.getId();
        this.paymentId = reservation.getPaymentId();
        this.totalPrice = reservation.getTotalPrice();
        this.reservedAt = reservation.getReservedAt();
        this.expireAt = reservation.getExpireDate();
    }
}
