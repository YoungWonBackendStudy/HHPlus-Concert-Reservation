package io.hhplus.concert.domain.concert;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConcertSchedule {
    Long id;
    Long concertId;
    String place;
    Date reservationStDate;
    Date reservationEndDate;
    Date concertDate;
}
