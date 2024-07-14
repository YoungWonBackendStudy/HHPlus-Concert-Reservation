package io.hhplus.concert.domain.concert;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConcertSeat {
    Long id;
    Long ConcertScheduleId;
    String location;
    Long price;
}
