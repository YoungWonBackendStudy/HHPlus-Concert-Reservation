package io.hhplus.concert.facade.concert;

import io.hhplus.concert.domain.concert.Concert;
import lombok.Getter;

@Getter
public class ConcertDto {
    Long id;
    String name;
    String description;

    public ConcertDto(Concert domain) {
        this.id = domain.getId();
        this.name = domain.getName();
        this.description = domain.getDescription();
    }
}
