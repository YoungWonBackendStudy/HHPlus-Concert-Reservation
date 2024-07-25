package io.hhplus.concert.domain.concert;

import lombok.Getter;

import java.util.Date;

@Getter
public class ConcertSeat {
    Long id;
    Long concertPlaceId;
    String location;
    Long version;
    Long price;

    public ConcertSeat(Long id, Long concertPlaceId, String location, Long version, Long price) {
        this.id = id;
        this.concertPlaceId = concertPlaceId;
        this.version = version;
        this.location = location;
        this.price = price;
    }
}