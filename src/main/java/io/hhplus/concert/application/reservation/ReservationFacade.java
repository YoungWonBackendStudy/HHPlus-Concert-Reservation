package io.hhplus.concert.application.reservation;

import io.hhplus.concert.domain.common.RedissonLockClient;
import io.hhplus.concert.domain.concert.ConcertSeat;
import io.hhplus.concert.domain.concert.ConcertService;
import io.hhplus.concert.domain.reservation.Reservation;
import io.hhplus.concert.domain.reservation.ReservationService;
import io.hhplus.concert.domain.reservation.ReservationTicket;
import io.hhplus.concert.support.config.MyRedisKeyspaceConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationFacade {
    private final ConcertService concertService;
    private final ReservationService reservationService;
    private final RedissonLockClient redissonLockClient;
    private final MyRedisKeyspaceConfig keyspace;

    public ReservationDto reserveSeats(long userId, List<Long> seatIds) {
        List<ConcertSeat> concertSeats = concertService.getConcertSeatsByIds(seatIds);
        RedissonLockClient.RedissonLockedCallable<Reservation> lockedReservationCall = () -> {
            Reservation reservation = reservationService.reserveConcertSeats(userId, concertSeats);
            concertService.reserveConcertSeats(concertSeats);
            return reservation;
        };

        var reservation =redissonLockClient.applyLock(keyspace.getReservation(), seatIds.stream().map(Object::toString).toList(), 0L, 3000L, lockedReservationCall);
        return new ReservationDto(reservation);
    }

    public void updateSeatsExpired() {
        var reservations = this.reservationService.expiresAndGetReservations();
        var seatIds = new LinkedList<Long>();
        for(var reservation : reservations) {
            seatIds.addAll(reservation.getReservationTickets().stream().map(ReservationTicket::getConcertSeatId).toList());
        }
        this.concertService.expireConcertSeats(seatIds);
    }
}
