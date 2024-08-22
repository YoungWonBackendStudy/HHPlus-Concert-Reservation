package io.hhplus.concert.domain.concert;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConcertSeat {
    Long id;
    Long concertScheduleId;
    String location;
    Long price;
    Boolean reserved;

    public void reserved() {
        this.reserved = true;
    }

    public void reservationExpired() {
        this.reserved = false;
    }
}