package io.hhplus.concert.domain.reservation;

import io.hhplus.concert.domain.concert.ConcertSeat;
import io.hhplus.concert.support.exception.CustomBadRequestException;
import io.hhplus.concert.support.exception.ExceptionCode;
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
        if(concertSeat.getReserved()) throw new CustomBadRequestException(ExceptionCode.SEAT_ALREADY_RESERVED);

        this.reservationId = reservationId;
        this.concertScheduleId = concertSeat.getConcertScheduleId();
        this.concertSeatId = concertSeat.getId();
        this.price = concertSeat.getPrice();
        this.seatLocation = concertSeat.getLocation();
    }

}
