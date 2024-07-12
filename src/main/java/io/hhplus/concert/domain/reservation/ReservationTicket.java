package io.hhplus.concert.domain.reservation;

import io.hhplus.concert.domain.concert.ConcertSeat;
import lombok.Getter;

@Getter
public class ReservationTicket {
    Long id;
    Long reservationId;
    Long concertScheduleId;
    Long concertSeatId;
    String seatLocation;
    long price;

    public ReservationTicket(long reservationId, ConcertSeat concertSeat) {
        this.reservationId = reservationId;
        this.concertSeatId = concertSeat.getId();
        this.price = concertSeat.getPrice();
        this.seatLocation = concertSeat.getLocation();
    }
}
