package io.hhplus.concert.application.concert;

import io.hhplus.concert.domain.concert.Concert;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class ConcertDto implements Serializable {
    Long id;
    String name;
    String description;

    public ConcertDto(Concert domain) {
        this.id = domain.getId();
        this.name = domain.getName();
        this.description = domain.getDescription();
    }
}
