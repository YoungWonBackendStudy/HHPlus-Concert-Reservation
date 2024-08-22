package io.hhplus.concert.interfaces.presentation.reservation.dto;

import java.util.Date;
import java.util.List;

import io.hhplus.concert.application.reservation.ReservationDto;

public record ReservationResponse(
    long reservationId,
    long totalPrice,
    Date reservedAt,
    Date expireAt,
    List<ReservedSeatInfo> reservedSeats
) {
    public static ReservationResponse of(ReservationDto facadeDto) {
        var reservedSeats = facadeDto.getReservationTickets()
            .stream().map(ReservedSeatInfo::of).toList();

        return new ReservationResponse(facadeDto.getReservationId(), facadeDto.getTotalPrice(), facadeDto.getReservedAt(), facadeDto.getExpireAt(), reservedSeats);
    }
}