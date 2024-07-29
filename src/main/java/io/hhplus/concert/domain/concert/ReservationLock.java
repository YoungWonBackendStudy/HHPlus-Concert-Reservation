package io.hhplus.concert.domain.concert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.redisson.api.RLock;

@Getter
@AllArgsConstructor
public class ReservationLock {
    RLock lock;
    Long seatId;
}
