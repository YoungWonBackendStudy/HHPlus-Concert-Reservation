package io.hhplus.concert.infra.reservation;

import java.util.Date;

import io.hhplus.concert.domain.reservation.Reservation;
import io.hhplus.concert.domain.reservation.ReservationTicket;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "reservation")
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "user_id")
    long userId;

    @Column(name = "reserved_at")
    Date reservedAt;

    @Column(name = "completed_at")
    Date completedAt;

    public ReservationEntity(Reservation domain) {
        this.id = domain.getId();
        this.userId = domain.getUserId();
        this.reservedAt = domain.getReservedAt();
        this.completedAt = domain.getCompletedAt();
    }

    public Reservation toDomain(List<ReservationTicket> reservationTickets) {
        return new Reservation(this.id, this.userId, this.reservedAt, this.completedAt, reservationTickets);
    }
}
