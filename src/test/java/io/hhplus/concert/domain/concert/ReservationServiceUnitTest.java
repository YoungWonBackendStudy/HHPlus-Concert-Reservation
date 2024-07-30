package io.hhplus.concert.domain.concert;

import io.hhplus.concert.domain.reservation.Reservation;
import io.hhplus.concert.domain.reservation.ReservationRepository;
import io.hhplus.concert.domain.reservation.ReservationService;
import io.hhplus.concert.domain.reservation.ReservationTicket;
import io.hhplus.concert.support.exception.ExceptionCode;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReservationServiceUnitTest {
    ReservationService reservationService;
    ReservationRepository mockReservationRepository;
    ConcertRepository mockConcertRepository;

    public ReservationServiceUnitTest() {
        mockReservationRepository = mock(ReservationRepository.class);
        mockConcertRepository = mock(ConcertRepository.class);
        reservationService = new ReservationService(mockReservationRepository, mockConcertRepository);
    }

    @Test
    @DisplayName("콘서트 좌석 예약 성공")
    void testReservation() {
        //given
        long userId = 0;
        long concertScheduleId = 0;
        List<ConcertSeat> seats = List.of(
            new ConcertSeat(0L, concertScheduleId, "R1", 100000L, false)
            , new ConcertSeat(1L, concertScheduleId, "R2", 120000L, false)
        );
        List<ReservationTicket> tickets = seats
                .stream().map(seat -> new ReservationTicket(0L, seat))
                .toList();
        Reservation reservation = new Reservation(0L, userId, new Date(), null, tickets);

        when(mockReservationRepository.saveReservation(any(Reservation.class))).thenReturn(reservation);

        //when
        var reservationRes = reservationService.reserveConcertSeats(userId, seats);

        //then
        assertThat(reservationRes).isNotNull();
        assertThat(reservationRes.getReservationTickets()).isNotEmpty();
        assertThat(reservationRes.getTotalPrice()).isEqualTo(100000L + 120000L);
    }

    @Test
    @DisplayName("이미 예약된 좌석을 예약시도할 경우 실패")
    void testReservationAlreadyReserved() {
        //given
        long userId = 0;
        long concertPlaceId = 0;
        long reservationId = 0;
        List<ConcertSeat> seats = List.of(
                new ConcertSeat(0L, concertPlaceId, "R1", 100000L, false)
                , new ConcertSeat(1L, concertPlaceId, "R2", 120000L, false)
        );
        List<ReservationTicket> tickets = seats
                .stream().map(seat -> new ReservationTicket(reservationId, seat))
                .toList();

        seats.forEach(ConcertSeat::reserved);
        Reservation reservation = new Reservation(reservationId, userId, new Date(), null, tickets);

        when(mockReservationRepository.saveReservation(any(Reservation.class))).thenReturn(reservation);

        //when
        ThrowableAssert.ThrowingCallable reservationRes = () -> reservationService.reserveConcertSeats(userId, seats);

        //then
        assertThatThrownBy(reservationRes).hasMessage(ExceptionCode.SEAT_ALREADY_RESERVED.getMessage());
    }
}
