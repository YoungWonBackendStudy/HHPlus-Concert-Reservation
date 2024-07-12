package io.hhplus.concert.facade.concert;

import io.hhplus.concert.domain.concert.ConcertSeat;
import lombok.Getter;

@Getter
public class ConcertSeatDto {
    Long id;
    String location;
    boolean reserved;
    Long price;

    public ConcertSeatDto(ConcertSeat domain, boolean reserved){
        this.id = domain.getId();
        this.location = domain.getLocation();
        this.price = domain.getPrice();
        this.reserved = reserved;
    }
}
