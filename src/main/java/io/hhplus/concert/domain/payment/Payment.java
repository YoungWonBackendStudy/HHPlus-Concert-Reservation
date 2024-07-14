package io.hhplus.concert.domain.payment;

import java.util.Date;

import io.hhplus.concert.domain.reservation.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Payment {
    long id;
    long price;
    long reservationId;
    Date paidAt;

    public Payment(Reservation reservation) {
        this.reservationId = reservation.getId();
        this.price = reservation.getTotalPrice();
        this.paidAt = new Date();
    }
}
