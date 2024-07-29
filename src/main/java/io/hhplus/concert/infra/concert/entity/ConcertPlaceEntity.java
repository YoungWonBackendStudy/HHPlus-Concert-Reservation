package io.hhplus.concert.infra.concert.entity;

import io.hhplus.concert.domain.concert.ConcertPlace;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "concert_place")
@Getter
@NoArgsConstructor
public class ConcertPlaceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "place")
    String place;

    public ConcertPlaceEntity(ConcertPlace domain) {
        this.id = domain.getId();
        this.place = domain.getPlace();
    }

    public ConcertPlace toDomain() {
        return new ConcertPlace(this.id, this.place);
    }
}
