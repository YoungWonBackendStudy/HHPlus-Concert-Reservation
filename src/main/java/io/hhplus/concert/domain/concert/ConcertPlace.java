package io.hhplus.concert.domain.concert;

import lombok.Getter;

@Getter
public class ConcertPlace {
    Long id;
    String place;

    public ConcertPlace(Long id, String place) {
        this.id = id;
        this.place = place;
    }
}
