package io.hhplus.concert.application.concert;

import java.util.Date;

import io.hhplus.concert.domain.concert.ConcertSchedule;
import lombok.Getter;

@Getter
public class ConcertScheduleDto {
    Long id;
    String place;
    Date concertDate;

    public ConcertScheduleDto(ConcertSchedule domain) {
        this.id = domain.getId();
        this.place = domain.getPlace();
        this.concertDate = domain.getConcertDate();
    }
}
