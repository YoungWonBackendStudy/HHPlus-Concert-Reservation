package io.hhplus.concert.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import io.hhplus.concert.application.reservation.ReservationFacade;
import io.hhplus.concert.domain.reservation.ReservationRepository;
import io.hhplus.concert.support.exception.CustomBadRequestException;
import io.hhplus.concert.support.exception.ExceptionCode;

@SpringBootTest
@Sql(scripts = "classpath:testinit.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ReservationFacadeIntegTest {
    @Autowired
    ReservationFacade reservationFacade;

    @Autowired
    ReservationRepository repository;

    @Test
    @DisplayName("두 좌석에 대한 예약을 두번 시도할 경우 -> 1번은 정상 예약, 2번째는 이미 예약된 좌석 에러")
    void testReservationAndDuplicate() {
        //given
        long userId = 0;
        long concertScheduleId = 0;
        List<Long> reservationSeats = List.of(0l, 1l);

        //when
        var reservation = reservationFacade.reserveSeats(userId, concertScheduleId, reservationSeats);

        //then
        assertThat(reservation).isNotNull();

        //when
        ThrowableAssert.ThrowingCallable dupReservation = () -> reservationFacade.reserveSeats(userId, concertScheduleId, reservationSeats);

        //then
        assertThatThrownBy(dupReservation).hasMessage(ExceptionCode.RESERVATION_ALREADY_RESERVED.getMessage());
    }

    @Test
    @DisplayName("동시성 테스트: 10명이 동시에 2개의 같은 좌석을 예약할 때 1명만 성공 이후는 실패 -> Ticket은 2개만 발급")
    void testConsistent() throws InterruptedException {
         //given
         long concertScheduleId = 0;
         List<Long> reservationSeats = List.of(2l, 3l);

         int userCnt = 10;
         List<Long> userIdsToApply = new ArrayList<>(userCnt);
            for(long i = 0; i < userCnt; i++) {
            userIdsToApply.add(i);
        }
         ExecutorService executorService = Executors.newFixedThreadPool(4);
         CountDownLatch latch = new CountDownLatch(userCnt);
 
 
         //when: 10번 동시에 예약할 때
         var ticketsBefore = repository.getReservedTicketsByConcertScheduleId(concertScheduleId);
         for (Long userId : userIdsToApply) {
             executorService.submit(() -> {
                try{ reservationFacade.reserveSeats(userId, concertScheduleId, reservationSeats); }
                catch(CustomBadRequestException e) {
                } finally {
                    latch.countDown();
                }
             });
         }
         latch.await();
         executorService.shutdown();
 
         //then: 1회의 예약 제외 모두 실패 -> Ticket은 2개만 발급
         var ticketsAfter = repository.getReservedTicketsByConcertScheduleId(concertScheduleId);
         assertThat(ticketsAfter.size()).isEqualTo(ticketsBefore.size() + reservationSeats.size());
    }

}
