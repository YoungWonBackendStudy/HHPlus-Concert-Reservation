package io.hhplus.concert.interfaces.presentation.concert.dto;

import io.hhplus.concert.application.concert.ConcertDto;

public record ConcertResponse(
    long id,
    String name,
    String description
) {
    public static ConcertResponse of(ConcertDto facadeDto) {
        return new ConcertResponse(facadeDto.getId(), facadeDto.getName(), facadeDto.getDescription());
    }
}
