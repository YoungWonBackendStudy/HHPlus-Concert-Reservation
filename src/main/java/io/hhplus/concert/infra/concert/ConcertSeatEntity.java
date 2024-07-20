package io.hhplus.concert.infra.concert;

import io.hhplus.concert.domain.concert.ConcertSeat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "concert_seat")
@NoArgsConstructor
public class ConcertSeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "concert_schedule_id", nullable = false)
    long concertScheduleId;

    @Column(name = "location")
    String location;

    @Column(name = "price")
    Long price;

    public ConcertSeatEntity(ConcertSeat concertSeat) {
        this.id = concertSeat.getId();
        this.concertScheduleId = concertSeat.getConcertScheduleId();
        this.location = concertSeat.getLocation();
        this.price = concertSeat.getPrice();
    }

    public ConcertSeat toDomain() {
        return new ConcertSeat(this.id, this.concertScheduleId, this.location, this.price);
    }
}
