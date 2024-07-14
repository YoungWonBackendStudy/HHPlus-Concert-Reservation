package io.hhplus.concert.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.hhplus.concert.facade.concert.ConcertFacade;
import io.hhplus.concert.facade.concert.ConcertSeatDto;
import io.hhplus.concert.facade.payment.PaymentFacade;
import io.hhplus.concert.facade.reservation.ReservationFacade;
import io.hhplus.concert.facade.waiting.WaitingFacade;

@SpringBootTest
public class PaymentFacadeIntegTest {
    PaymentFacade paymentFacade;
    WaitingFacade waitingFacade;
    ReservationFacade reservationFacade;
    ConcertFacade concertFacade;

    public PaymentFacadeIntegTest(PaymentFacade paymentFacade, ReservationFacade reservationFacade, WaitingFacade waitingFacade, ConcertFacade concertFacade) {
        this.paymentFacade = paymentFacade;
        this.waitingFacade = waitingFacade;
        this.reservationFacade = reservationFacade;
        this.concertFacade = concertFacade;
    }

    @Test
    @DisplayName("5개좌석 예약에 대한 정상 결제 && 중복 결제 테스트")
    void testPayment() {
        //given
        long userId = 0l;
        long concertScheduleId = 0;
        
        var waiting = waitingFacade.getWaitingToken(userId);
        waitingFacade.scheduleWaiting();

        var concertSeats = concertFacade.getConcertSeats(waiting.getToken(), 0l);
        assertThat(concertSeats.size()).isLessThan(5);
        List<Long> seatsToReserve = concertSeats.subList(0, 5).stream().map(ConcertSeatDto::getId).toList();
        
        var reservation = reservationFacade.reserveSeats(waiting.getToken(), concertScheduleId, seatsToReserve);
        
        //when
        var payment = paymentFacade.placePayment(waiting.getToken(), reservation.getReservationId());

        //then
        assertThat(payment).isNotNull();
        assertThat(payment.getTotalPrice()).isEqualTo(reservation.getTotalPrice());

        //when: 중복 결제
        ThrowingCallable dupPayment = () -> paymentFacade.placePayment(waiting.getToken(), reservation.getReservationId());

        //then
        assertThatThrownBy(dupPayment).hasMessage("이미 완료된 예약입니다.");
    }
}
