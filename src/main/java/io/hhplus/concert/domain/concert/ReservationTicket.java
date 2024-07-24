package io.hhplus.concert.domain.concert;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationTicket {
    Long id;
    Long reservationId;
    Long concertScheduleId;
    Long concertSeatId;
    String seatLocation;
    long price;

    public ReservationTicket(Long reservationId, ConcertSeat concertSeat) {
        this.reservationId = reservationId;
        this.concertScheduleId = concertSeat.getConcertPlaceId();
        this.concertSeatId = concertSeat.getId();
        this.price = concertSeat.getPrice();
        this.seatLocation = concertSeat.getLocation();
    }

}
