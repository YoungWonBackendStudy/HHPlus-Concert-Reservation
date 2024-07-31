package io.hhplus.concert.application.reservation;

import io.hhplus.concert.domain.common.RedissonLockClient;
import io.hhplus.concert.domain.concert.ConcertSeat;
import io.hhplus.concert.domain.concert.ConcertService;
import io.hhplus.concert.domain.reservation.Reservation;
import io.hhplus.concert.domain.reservation.ReservationService;
import io.hhplus.concert.domain.reservation.ReservationTicket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class ReservationFacade {
    private final ConcertService concertService;
    private final ReservationService reservationService;

    private final RedissonLockClient redissonLockClient;

    @Value("${spring.data.redis.keyspace.reservation}")
    String keySpaceReservation;

    public ReservationFacade(ConcertService concertService, ReservationService reservationService, RedissonLockClient redissonLockClient) {
        this.concertService = concertService;
        this.reservationService = reservationService;
        this.redissonLockClient = redissonLockClient;
    }

    public ReservationDto reserveSeats(long userId, List<Long> seatIds) {
        List<ConcertSeat> concertSeats = concertService.getConcertSeatsByIds(seatIds);
        RedissonLockClient.RedissonLockedCallable<Reservation> lockedReservationCall = () -> {
            Reservation reservation = reservationService.reserveConcertSeats(userId, concertSeats);
            concertService.reserveConcertSeats(concertSeats);
            return reservation;
        };

        var reservation =redissonLockClient.applyLock(keySpaceReservation, seatIds.stream().map(Object::toString).toList(), 0L, 3000L, lockedReservationCall);
        return new ReservationDto(reservation);
    }

    public void updateSeatsExpired() {
        var reservations = this.reservationService.getExpiredReservations();
        var seatIds = new LinkedList<Long>();
        for(var reservation : reservations) {
            seatIds.addAll(reservation.getReservationTickets().stream().map(ReservationTicket::getConcertSeatId).toList());
        }
        this.concertService.expireConcertSeats(seatIds);
    }
}
