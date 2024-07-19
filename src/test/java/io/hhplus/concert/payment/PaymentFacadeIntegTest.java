package io.hhplus.concert.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import io.hhplus.concert.application.concert.ConcertFacade;
import io.hhplus.concert.application.concert.ConcertSeatDto;
import io.hhplus.concert.application.payment.PaymentFacade;
import io.hhplus.concert.application.reservation.ReservationFacade;
import io.hhplus.concert.application.user.UserAssetFacade;
import io.hhplus.concert.application.waiting.WaitingFacade;
import io.hhplus.concert.support.exception.CustomBadRequestException;
import io.hhplus.concert.support.exception.ExceptionCode;

@SpringBootTest
@Sql(scripts = "classpath:testinit.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class PaymentFacadeIntegTest {
    @Autowired
    PaymentFacade paymentFacade;
    @Autowired
    WaitingFacade waitingFacade;
    @Autowired
    ReservationFacade reservationFacade;
    @Autowired
    UserAssetFacade userAssetFacade;

    @Autowired
    ConcertFacade concertFacade;

    @Test
    @DisplayName("좌석 예약을 두번 결제시도할 경우 1번째는 정상 결제 && 두번째는 토큰 만료 (결제 완료 시 토큰 만료)")
    void testPayment() {
        //given
        long userId = 0l;
        long concertScheduleId = 0;
        
        var waiting = waitingFacade.getWaitingToken(userId);
        waitingFacade.scheduleWaiting();

        var concertSeats = concertFacade.getConcertSeats(0l);
        List<Long> seatsToReserve = concertSeats
            .stream().filter(seat -> seat.isReserved() == false)
            .map(ConcertSeatDto::getId).toList().subList(0, 1);

        assertThat(concertSeats.size()).isGreaterThanOrEqualTo(1); //guard assertion
        
        var reservation = reservationFacade.reserveSeats(userId, concertScheduleId, seatsToReserve);

        userAssetFacade.chargeBalance(userId, reservation.getTotalPrice());
        
        //when
        var payment = paymentFacade.placePayment(waiting.getToken(), reservation.getReservationId());

        //then
        assertThat(payment).isNotNull();
        assertThat(payment.getTotalPrice()).isEqualTo(reservation.getTotalPrice());

        //when: 중복 결제
        ThrowingCallable dupPayment = () -> paymentFacade.placePayment(waiting.getToken(), reservation.getReservationId());

        //then
        assertThatThrownBy(dupPayment).hasMessage(ExceptionCode.WAITING_TOKEN_EXPIRED.getMessage());
    }

    @Test
    @DisplayName("하나의 결제에 대한 5번 동시 결제 시도할 경우 한번만 결제 됨")
    void testPaymentConsistent() throws InterruptedException {
        //given
        long userId = 0l;
        long concertScheduleId = 0;
        int executionCnt = 5;
        var waiting = waitingFacade.getWaitingToken(userId);
        waitingFacade.scheduleWaiting();

        var concertSeats = concertFacade.getConcertSeats(0l);
        List<Long> seatsToReserve = concertSeats
            .stream().filter(seat -> seat.isReserved() == false)
            .map(ConcertSeatDto::getId).toList().subList(0, 1);

        assertThat(concertSeats.size()).isGreaterThanOrEqualTo(1);
        
        var reservation = reservationFacade.reserveSeats(userId, concertScheduleId, seatsToReserve);

        userAssetFacade.chargeBalance(userId, reservation.getTotalPrice());

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(executionCnt);
        
        //when
        long balanceBefore = userAssetFacade.getBalance(userId);
        for (int i =0; i < executionCnt; i++) {
            executorService.submit(() -> {
                try{ 
                    paymentFacade.placePayment(waiting.getToken(), reservation.getReservationId());
                }
                catch(CustomBadRequestException e) {
                }finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        //then
        long balanceAfter = userAssetFacade.getBalance(userId);
        assertThat(balanceAfter).isEqualTo(balanceBefore - reservation.getTotalPrice());
    }
}
