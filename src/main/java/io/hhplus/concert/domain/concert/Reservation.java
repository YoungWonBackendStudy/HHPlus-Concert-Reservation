package io.hhplus.concert.domain.concert;

import java.util.Date;
import java.util.List;

import io.hhplus.concert.support.exception.CustomBadRequestException;
import io.hhplus.concert.support.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Reservation {
    static final long expireDurationInMilli = 5 * 60 * 1000L;
    long id;
    long userId;
    Date reservedAt;
    Date completedAt;
    List<ReservationTicket> reservationTickets;

    public Reservation(long userId) {
        this.userId = userId;
        this.reservedAt = new Date();
        reservationTickets = List.of();
    }

    public void makeTickets(List<ConcertSeat> seats) {
        this.reservationTickets = seats.stream().map(seat -> new ReservationTicket(this.id ,seat)).toList();
    }

    public long getTotalPrice() {
        long totalPrice = 0;
        for (ReservationTicket reservationTicket : this.reservationTickets) {
            totalPrice += reservationTicket.getPrice();
        }

        return totalPrice;
    } 

    public void completed() {
        this.completedAt = new Date();
    }

    public Date getExpireDate() {
        return new Date(this.reservedAt.getTime() + expireDurationInMilli);
    }

    public void validatePayable() {
        if(this.completedAt != null)
            throw new CustomBadRequestException(ExceptionCode.PAYMENT_ALREADY_COMPLETED);
        if(System.currentTimeMillis() > this.getExpireDate().getTime()) 
            throw new CustomBadRequestException(ExceptionCode.RESERVATION_EXPIRED);
    }
}
