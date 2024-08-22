package io.hhplus.concert.infra.payment;

import java.util.Date;

import io.hhplus.concert.domain.payment.Payment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "payment")
@NoArgsConstructor
public class PaymentEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "price")
    Long price;

    @Column(name = "reservation_id")
    Long reservationId;

    @Column(name = "paid_at")
    Date paidAt;

    public PaymentEntity(Payment domain) {
        this.id = domain.getId();
        this.price = domain.getPrice();
        this.reservationId = domain.getReservationId();
        this.paidAt = domain.getPaidAt();
    }

    public Payment toDomain() {
        return new Payment(this.id, this.price, this.reservationId, this.paidAt);
    }
}
