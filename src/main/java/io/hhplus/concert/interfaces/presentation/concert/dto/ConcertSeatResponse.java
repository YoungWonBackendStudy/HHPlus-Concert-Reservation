package io.hhplus.concert.interfaces.presentation.concert.dto;

import io.hhplus.concert.application.concert.ConcertSeatDto;

public record ConcertSeatResponse(
    long id,
    String location,
    boolean reserved,
    long price
) {
    public static ConcertSeatResponse of(ConcertSeatDto facadeDto) {
        return new ConcertSeatResponse(facadeDto.getId(), facadeDto.getLocation(), facadeDto.isReserved(), facadeDto.getPrice());
    }
}
