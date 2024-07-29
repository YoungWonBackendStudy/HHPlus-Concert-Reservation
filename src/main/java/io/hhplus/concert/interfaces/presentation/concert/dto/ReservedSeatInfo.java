package io.hhplus.concert.interfaces.presentation.concert.dto;

import io.hhplus.concert.application.concert.ReservationTicketDto;

public record ReservedSeatInfo(
    long id,
    String location,
    long price
) {
    public static ReservedSeatInfo of(ReservationTicketDto facadeDto) {
        return new ReservedSeatInfo(facadeDto.getSeatId(), facadeDto.getSeatLocation(), facadeDto.getPrice());
    }
}