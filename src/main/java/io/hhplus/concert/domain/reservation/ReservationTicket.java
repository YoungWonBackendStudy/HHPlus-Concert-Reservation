package io.hhplus.concert.domain.reservation;

import io.hhplus.concert.domain.concert.ConcertSeat;
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

    public ReservationTicket(long reservationId, ConcertSeat concertSeat) {
        this.reservationId = reservationId;
        this.concertScheduleId = concertSeat.getConcertScheduleId();
        this.concertSeatId = concertSeat.getId();
        this.price = concertSeat.getPrice();
        this.seatLocation = concertSeat.getLocation();
    }
}
