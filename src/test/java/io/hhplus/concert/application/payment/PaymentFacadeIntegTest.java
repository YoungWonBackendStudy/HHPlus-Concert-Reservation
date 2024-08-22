package io.hhplus.concert.application.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.hhplus.concert.application.concert.ConcertFacade;
import io.hhplus.concert.application.concert.ConcertSeatDto;
import io.hhplus.concert.application.queue.QueueFacade;
import io.hhplus.concert.application.reservation.ReservationFacade;
import io.hhplus.concert.application.user.UserAssetFacade;
import io.hhplus.concert.domain.payment.PaymentMessageStatus;
import io.hhplus.concert.domain.payment.PaymentSuccessMessage;
import io.hhplus.concert.infra.payment.PaymentSuccessOutboxJpaRepository;
import io.hhplus.concert.support.exception.CustomBadRequestException;
import io.hhplus.concert.support.exception.ExceptionCode;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:testinit.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@EmbeddedKafka(partitions = 1, topics = { "payment-success" })
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
    @Autowired
    private PaymentSuccessOutboxJpaRepository paymentSuccessOutboxJpaRepository;

    @Test
    @DisplayName("결제 성공 테스트")
    void testPaymentSuccessEvents() throws JsonProcessingException {
        //given
        long userId = 0L;
        long concertScheduleId = 0L;

        var queueToken = queueFacade.getQueueToken(null);
        queueFacade.scheduleWaitingQueue();
        assertThatThrownBy(() -> queueFacade.getQueueToken(queueToken.getToken()))
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
        var payment = paymentFacade.placePayment(queueToken.getToken(), userId, reservation.getReservationId());

        //then
        assertThat(payment.getTotalPrice()).isEqualTo(reservation.getTotalPrice());

        var userAssetAfterPayment = userAssetFacade.getBalance(userId);
        assertThat(userAssetAfterPayment).isEqualTo(userAssetBeforePayment - payment.getTotalPrice());

        //비동기 Application Event 확인 ( 토큰 만료, Outbox)
        await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> {
           assertThatThrownBy(() -> queueFacade.getActiveToken(queueToken.getToken())).hasMessage(ExceptionCode.ACTIVE_TOKEN_NOT_FOUND.getMessage());

           var paymentMessageProduced = paymentSuccessOutboxJpaRepository.findByPaymentId(payment.getId());
           assertThat(paymentMessageProduced).isNotNull();
           assertThat(paymentMessageProduced.getStatus()).isEqualTo(PaymentMessageStatus.PUBLISHED);
        });
    }

    @Test
    @DisplayName("좌석 예약을 두번 결제시도할 경우 1번째는 정상 결제 && 두번째는 이미 결제된 예약입니다 예외 발생")
    void testPaymentAndDuplicatedPayment() throws JsonProcessingException {
        //given
        long userId = 0L;
        long concertScheduleId = 0L;
        
        var queueToken = queueFacade.getQueueToken(null);
        queueFacade.scheduleWaitingQueue();
        assertThatThrownBy(() -> queueFacade.getQueueToken(queueToken.getToken()))
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
        var payment = paymentFacade.placePayment(queueToken.getToken(), userId, reservation.getReservationId());

        //then
        assertThat(payment).isNotNull();

        //when: 중복 결제
        ThrowingCallable dupPayment = () -> paymentFacade.placePayment(queueToken.getToken(), userId, reservation.getReservationId());

        //then
        assertThatThrownBy(dupPayment).hasMessage(ExceptionCode.PAYMENT_ALREADY_COMPLETED.getMessage());
    }

    @Test
    @DisplayName("동시성 테스트: 하나의 예약에 대해 5번 동시 결제 시도할 경우 한번만 결제 됨")
    void testPaymentConsistent() throws InterruptedException {
        //given
        long userId = 0L;
        long concertScheduleId = 0L;
        int executionCnt = 5;
        var queue = queueFacade.getQueueToken(null);
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
                    paymentFacade.placePayment(queue.getToken(), userId, reservation.getReservationId());
                }
                catch(CustomBadRequestException | JsonProcessingException e) {
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
