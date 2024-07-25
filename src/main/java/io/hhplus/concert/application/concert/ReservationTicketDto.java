package io.hhplus.concert.application.concert;

import io.hhplus.concert.domain.concert.ReservationTicket;
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
    
