package io.hhplus.concert.concert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;

import io.hhplus.concert.domain.concert.*;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.hhplus.concert.support.exception.ExceptionCode;

public class ReservationServiceUnitTest {
    ReservationService reservationService;
    ReservationRepository mockReservationRepository;

    public ReservationServiceUnitTest() {
        mockReservationRepository = mock(ReservationRepository.class);
        reservationService = new ReservationService(mockReservationRepository);
    }

    @Test
    @DisplayName("콘서트 좌석 2개 예약 성공")
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
    @DisplayName("5분전 예약에 대한 예약 만료 테스트")
    void testReservationExpired() {
        //given
        when(mockReservationRepository.getAndLockById(0L)).thenReturn(
            new Reservation(0L, 0L, new Date(System.currentTimeMillis() - 5 * 60 * 1000L - 1), null, List.of())
        );

        //when
        ThrowableAssert.ThrowingCallable result = () -> reservationService.validateAndGetReservation(0L);
        
        //then
        assertThatThrownBy(result).hasMessage(ExceptionCode.RESERVATION_EXPIRED.getMessage());
    }
}
