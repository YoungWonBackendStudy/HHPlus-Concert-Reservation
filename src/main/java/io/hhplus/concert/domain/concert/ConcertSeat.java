package io.hhplus.concert.domain.concert;

import lombok.Getter;

@Getter
public class ConcertSeat {
    Long id;
    Long concertPlaceId;
    String location;
    Long price;

    public ConcertSeat(Long id, Long concertPlaceId, String location, Long price) {
        this.id = id;
        this.concertPlaceId = concertPlaceId;
        this.location = location;
        this.price = price;
    }
}