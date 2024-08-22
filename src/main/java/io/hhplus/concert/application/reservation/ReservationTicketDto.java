package io.hhplus.concert.application.reservation;

import io.hhplus.concert.domain.reservation.ReservationTicket;
import lombok.Getter;

@Getter
public class ReservationTicketDto {
    long seatId;
    String seatLocation;
    long price;

    public ReservationTicketDto(ReservationTicket domain) {
        this.seatId = domain.getConcertSeatId();
        this.seatLocation = domain.getSeatLocation();
        this.price = domain.getPrice();
    }
}
    
