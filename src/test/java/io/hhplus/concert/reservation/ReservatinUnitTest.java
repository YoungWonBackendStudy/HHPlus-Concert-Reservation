package io.hhplus.concert.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.hhplus.concert.domain.concert.ConcertSeat;
import io.hhplus.concert.domain.reservation.Reservation;
import io.hhplus.concert.domain.reservation.ReservationRepository;
import io.hhplus.concert.domain.reservation.ReservationService;

public class ReservatinUnitTest {
    ReservationService reservationService;
    ReservationRepository mockReservationRepository;

    public ReservatinUnitTest() {
        mockReservationRepository = mock(ReservationRepository.class);
        reservationService = new ReservationService(mockReservationRepository);
    }

    @Test
    @DisplayName("콘서트 좌석 2개 예약 성공")
    void testReservation() {
        //given
        long userId = 0;
        long concertScheduleId = 0l;
        List<ConcertSeat> seats = List.of(
            new ConcertSeat(0l, concertScheduleId, "R1", 100000l)
            , new ConcertSeat(1l, concertScheduleId, "R2", 120000l)
        );
        when(mockReservationRepository.getReservedTicketsByConcertScheduleId(concertScheduleId)).thenReturn(List.of());
        when(mockReservationRepository.saveReservation(any(Reservation.class))).thenAnswer(returnsFirstArg());

        //when
        var reservation = reservationService.reserveConcertSeats(userId, concertScheduleId, seats);

        //then
        assertThat(reservation).isNotNull();
        assertThat(reservation.getReservationTickets()).isNotEmpty();
        assertThat(reservation.getTotalPrice()).isEqualTo(100000l + 120000l);
    }

    @Test
    @DisplayName("5분전 예약에 대한 예약 만료 테스트")
    void testReservationExpired() {
        //given
        when(mockReservationRepository.getById(0)).thenReturn(
            new Reservation(0l, 0l, 0l, new Date(System.currentTimeMillis() - 5 * 60 * 1000l - 1), null, null)
        );

        //when
        ThrowableAssert.ThrowingCallable result = () -> reservationService.validateAndGetReservation(0l);
        
        //then
        assertThatThrownBy(result).hasMessage("예약이 만료되었습니다.");
    }
}
