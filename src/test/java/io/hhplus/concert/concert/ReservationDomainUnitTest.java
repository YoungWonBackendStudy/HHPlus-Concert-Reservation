package io.hhplus.concert.concert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Date;
import java.util.List;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.hhplus.concert.domain.concert.ConcertSeat;
import io.hhplus.concert.domain.concert.Reservation;
import io.hhplus.concert.support.exception.ExceptionCode;

public class ReservationDomainUnitTest {
    @Test
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
    void testCompleted() {
        //given
        long userId = 0;
        Reservation reservation = new Reservation(userId);

        //when
        reservation.completed();

        //then
        assertThat(reservation.getCompletedAt()).isNotNull();
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
