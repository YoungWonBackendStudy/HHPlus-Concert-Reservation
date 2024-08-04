package io.hhplus.concert.application.payment;

import io.hhplus.concert.application.concert.ConcertFacade;
import io.hhplus.concert.application.concert.ConcertSeatDto;
import io.hhplus.concert.application.queue.QueueFacade;
import io.hhplus.concert.application.reservation.ReservationFacade;
import io.hhplus.concert.application.user.UserAssetFacade;
import io.hhplus.concert.support.exception.CustomBadRequestException;
import io.hhplus.concert.support.exception.ExceptionCode;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Sql(scripts = "classpath:testinit.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class PaymentFacadeIntegTest {
    @Autowired
    PaymentFacade paymentFacade;

    @Autowired
    QueueFacade queueFacade;

    @Autowired
    UserAssetFacade userAssetFacade;

    @Autowired
    ConcertFacade concertFacade;

    @Autowired
    ReservationFacade reservationFacade;

    @Test
    @DisplayName("좌석 예약을 두번 결제시도할 경우 1번째는 정상 결제 && 두번째는 토큰 만료 (결제 완료 시 토큰 만료)")
    void testPayment() {
        //given
        long userId = 0L;
        long concertScheduleId = 0L;
        
        var queueToken = queueFacade.getQueueToken(userId);
        queueFacade.scheduleWaitingQueue();
        assertThatThrownBy(() -> queueFacade.getQueueToken(userId))
                .hasMessage(ExceptionCode.TOKEN_IS_ACTIVATED.getMessage()); //guard assertion

        var concertSeats = concertFacade.getConcertSeats(concertScheduleId);
        var seatsToReserve = concertSeats.stream()
                .filter(seat -> !seat.getReserved())
                .map(ConcertSeatDto::getId)
                .toList();
        assertThat(concertSeats.size()).isGreaterThanOrEqualTo(1); //guard assertion
        
        var reservation = reservationFacade.reserveSeats(userId, seatsToReserve.subList(2,3));

        userAssetFacade.chargeBalance(userId, reservation.getTotalPrice() * 10);
        
        //when
        var userAssetBeforePayment = userAssetFacade.getBalance(userId);
        var payment = paymentFacade.placePayment(queueToken.getToken(), reservation.getReservationId());

        //then
        assertThat(payment).isNotNull();
        assertThat(payment.getTotalPrice()).isEqualTo(reservation.getTotalPrice());

        var userAssetAfterPayment = userAssetFacade.getBalance(userId);
        assertThat(userAssetAfterPayment).isEqualTo(userAssetBeforePayment - payment.getTotalPrice());

        //when: 중복 결제
        ThrowingCallable dupPayment = () -> paymentFacade.placePayment(queueToken.getToken(), reservation.getReservationId());

        //then
        assertThatThrownBy(dupPayment).hasMessage(ExceptionCode.ACTIVE_TOKEN_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("동시성 테스트: 하나의 예약에 대해 5번 동시 결제 시도할 경우 한번만 결제 됨")
    void testPaymentConsistent() throws InterruptedException {
        //given
        long userId = 0L;
        long concertScheduleId = 0L;
        int executionCnt = 5;
        var queue = queueFacade.getQueueToken(userId);
        queueFacade.scheduleWaitingQueue();

        var concertSeats = concertFacade.getConcertSeats(concertScheduleId);
        var seatsToReserve = concertSeats.stream()
                .filter(seat -> !seat.getReserved())
                .map(ConcertSeatDto::getId)
                .toList();

        assertThat(concertSeats.size()).isGreaterThanOrEqualTo(1);
        
        var reservation = reservationFacade.reserveSeats(userId, seatsToReserve.subList(3,4));

        userAssetFacade.chargeBalance(userId, reservation.getTotalPrice());

        ExecutorService executorService = Executors.newFixedThreadPool(executionCnt);
        CountDownLatch latch = new CountDownLatch(executionCnt);
        
        //when
        long balanceBefore = userAssetFacade.getBalance(userId);
        for (int i =0; i < executionCnt; i++) {
            executorService.submit(() -> {
                try{ 
                    paymentFacade.placePayment(queue.getToken(), reservation.getReservationId());
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
