package io.hhplus.concert.interfaces.presentation.reservation.dto;

import io.hhplus.concert.application.reservation.ReservationTicketDto;

public record ReservedSeatInfo(
    long id,
    String location,
    long price
) {
    public static ReservedSeatInfo of(ReservationTicketDto facadeDto) {
        return new ReservedSeatInfo(facadeDto.getSeatId(), facadeDto.getSeatLocation(), facadeDto.getPrice());
    }
}