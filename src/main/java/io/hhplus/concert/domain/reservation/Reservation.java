package io.hhplus.concert.domain.reservation;

import java.util.Date;
import java.util.List;

import io.hhplus.concert.domain.concert.ConcertSeat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Reservation {
    static final long expireDurationInMilli = 5 * 60 * 1000l;
    long id;
    long userId;
    long paymentId;
    Date reservedAt;
    Date completedAt;
    List<ReservationTicket> reservationTickets;

    public Reservation(long userId, List<ConcertSeat> seats) {
        this.userId = userId;
        this.reservedAt = new Date();
        this.reservationTickets = seats.stream().map(seat -> {
            return new ReservationTicket(userId, seat);
        }).toList();
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

    public void validate() {
        if(this.completedAt != null)
            throw new RuntimeException("이미 완료된 예약입니다.");
        if(System.currentTimeMillis() > this.getExpireDate().getTime()) 
            throw new RuntimeException("예약이 만료되었습니다.");
    }
}
