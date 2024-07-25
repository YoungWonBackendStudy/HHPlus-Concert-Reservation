package io.hhplus.concert.infra.concert.entity;

import io.hhplus.concert.domain.concert.ConcertSeat;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "concert_seat")
@NoArgsConstructor
public class ConcertSeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "concert_place_id", nullable = false)
    long concertPlaceId;

    @Column(name = "location")
    String location;

    @Column(name = "price")
    Long price;

    public ConcertSeatEntity(ConcertSeat concertSeat) {
        this.id = concertSeat.getId();
        this.concertPlaceId = concertSeat.getConcertPlaceId();
        this.location = concertSeat.getLocation();
        this.price = concertSeat.getPrice();
    }

    public ConcertSeat toDomain() {
        return new ConcertSeat(this.id, this.concertPlaceId, this.location, this.price);
    }
}
