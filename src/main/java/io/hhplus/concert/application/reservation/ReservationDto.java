package io.hhplus.concert.application.reservation;

import java.util.Date;
import java.util.List;

import io.hhplus.concert.domain.reservation.Reservation;
import lombok.Getter;

@Getter
public class ReservationDto {
    long reservationId;
    long totalPrice;
    Date reservedAt;
    Date expireAt;
    List<ReservationTicketDto> reservationTickets;

    public ReservationDto(Reservation reservation) {
        this.reservationId = reservation.getId();
        this.totalPrice = reservation.getTotalPrice();
        this.reservedAt = reservation.getReservedAt();
        this.expireAt = reservation.getExpireDate();
        this.reservationTickets = reservation.getReservationTickets()
            .stream().map(ReservationTicketDto::new)
            .toList();
    }
}
