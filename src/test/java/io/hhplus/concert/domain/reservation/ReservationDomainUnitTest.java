package io.hhplus.concert.domain.reservation;

import io.hhplus.concert.domain.concert.ConcertSeat;
import io.hhplus.concert.support.exception.ExceptionCode;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class ReservationDomainUnitTest {
    @Test
    @DisplayName("Ticket 전체 금액 조회 테스트")
    void testTotalPrice() {
        //given
        long userId = 0;
        List<ConcertSeat> seats = List.of(
            new ConcertSeat(0L, 0L, "R1", 10000L, false),
            new ConcertSeat(1L, 0L, "R2", 20000L, false)
        );
        
        //when
        Reservation reservation = new Reservation(userId);
        reservation.makeTickets(seats);

        //then
        assertThat(reservation.getTotalPrice()).isEqualTo(seats.get(0).getPrice() + seats.get(1).getPrice());
    }

    @Test
    @DisplayName("만료 일자 조회 테스트")
    void testExpireDate() {
        //given
        long userId = 0;
        long expireDurationInMilli = 5 * 60 * 1000L;
       

        //when
        Date expectedMinimum = new Date(System.currentTimeMillis() + expireDurationInMilli);
        Reservation reservation = new Reservation(userId);
        Date expectedMaximum = new Date(System.currentTimeMillis() + expireDurationInMilli);

        //then
        assertThat(reservation.getExpireDate()).isAfterOrEqualTo(expectedMinimum);
        assertThat(reservation.getExpireDate()).isBeforeOrEqualTo(expectedMaximum);
    }

    @Test
    @DisplayName("예약 완료/만료 처리 테스트")
    void testCompleted() {
        //given
        long userId = 0;
        Reservation reservation = new Reservation(userId);

        //when
        reservation.completed();

        //then
        assertThat(reservation.getStatus()).isEqualTo(Reservation.ReservationStatus.COMPLETED);
        assertThat(reservation.getCompletedAt()).isNotNull();

        //when
        reservation.expired();

        //then
        assertThat(reservation.getStatus()).isEqualTo(Reservation.ReservationStatus.EXPIRED);
    }

    @Test
    void testMakeTickets() {
         //given
         long userId = 0;
         List<ConcertSeat> seats = List.of(
             new ConcertSeat(0L, 0L, "R1", 10000L, false),
             new ConcertSeat(1L, 0L, "R2", 20000L, false)
         );
         
         //when
         Reservation reservation = new Reservation(userId);
         reservation.makeTickets(seats);
 
        //then
        assertThat(reservation.getReservationTickets()).isNotEmpty();
        assertThat(reservation.getReservationTickets().size()).isEqualTo(seats.size());
        assertThat(reservation.getReservationTickets().get(0).getConcertSeatId()).isEqualTo(seats.get(0).getId());
        assertThat(reservation.getReservationTickets().get(1).getConcertSeatId()).isEqualTo(seats.get(1).getId());
    }

    @Test
    @DisplayName("이미 결제까지 완료된 예약을 ValidatePayable 경우 ALREADY_COMPLETED 오류 발생 ")
    void testValidatePayableCompletedReservation() {
        //given
        long userId = 0;
        Reservation reservation = new Reservation(userId);
        reservation.completed();

        //when
        ThrowableAssert.ThrowingCallable result = reservation::validatePayable;

        //then
        assertThatThrownBy(result).hasMessage(ExceptionCode.PAYMENT_ALREADY_COMPLETED.getMessage());
    }

    @Test
    @DisplayName("결제가 필요한 예약을 validatePayable할 경우 정상 동작")
    void testValidatePayable() {
        //given
        long userId = 0;
        Reservation reservation = new Reservation(userId);

        //when
        ThrowableAssert.ThrowingCallable result = reservation::validatePayable;

        //then
        assertThatCode(result).doesNotThrowAnyException();
    }
}
