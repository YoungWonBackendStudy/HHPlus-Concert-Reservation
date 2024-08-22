package io.hhplus.concert.infra.concert;

import io.hhplus.concert.domain.concert.ConcertSchedule;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "concert_schedule")
@Getter
@NoArgsConstructor
public class ConcertScheduleEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "concert_id", nullable = false)
    long concertId;

    @Column(name = "place")
    String place;
    
    @Column(name = "reservation_st_date")
    Date reservationStDate;

    @Column(name = "reservation_end_date")
    Date reservationEndDate;

    @Column(name = "concert_date")
    Date concertDate;

    public ConcertScheduleEntity(ConcertSchedule domain) {
        this.id = domain.getId();
        this.concertId = domain.getConcertId();
        this.place = domain.getPlace();
        this.reservationStDate = domain.getReservationStDate();
        this.reservationEndDate = domain.getReservationEndDate();
        this.concertDate = domain.getConcertDate();
    }

    public ConcertSchedule toDomain() {
        return new ConcertSchedule(this.id, this.concertId, this.place, this.reservationStDate, this.reservationEndDate, this.concertDate);
    }
}
