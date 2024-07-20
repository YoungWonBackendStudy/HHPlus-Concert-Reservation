package io.hhplus.concert.infra.reservation;

import io.hhplus.concert.domain.reservation.ReservationTicket;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation_ticket")
@NoArgsConstructor
public class ReservationTicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "reservation_id")
    Long reservationId;

    @Column(name = "concert_schedule_id")
    Long concertScheduleId;

    @Column(name = "concert_seat_id")
    Long concertSeatId;

    @Column(name = "seat_location")
    String seatLocation;

    @Column(name = "price")
    long price;

    public ReservationTicketEntity(ReservationTicket domain) {
        this.id = domain.getId();
        this.reservationId = domain.getReservationId();
        this.concertScheduleId = domain.getConcertScheduleId();
        this.concertSeatId = domain.getConcertSeatId();
        this.seatLocation = domain.getSeatLocation();
        this.price = domain.getPrice();
    }

    public void setReservationId(long reservationId) {
        this.reservationId = reservationId;
    }

    public ReservationTicket toDomain() {
        return new ReservationTicket(this.id, this.reservationId, this.concertScheduleId, this.concertSeatId, this.seatLocation, this.price);
    }
}
