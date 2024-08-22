package io.hhplus.concert.application.concert;

import io.hhplus.concert.domain.concert.ConcertSeat;
import lombok.Getter;

@Getter
public class ConcertSeatDto {
    Long id;
    String location;
    Long price;
    Boolean reserved;

    public ConcertSeatDto(ConcertSeat domain){
        this.id = domain.getId();
        this.location = domain.getLocation();
        this.price = domain.getPrice();
        this.reserved = domain.getReserved();
    }
}
