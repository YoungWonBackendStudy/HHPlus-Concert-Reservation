package io.hhplus.concert.application.reservation;

import io.hhplus.concert.domain.reservation.ReservationRepository;
import io.hhplus.concert.support.exception.CustomBadRequestException;
import io.hhplus.concert.support.exception.ExceptionCode;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:testinit.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ReservationFacadeIntegTest {
    @Autowired
    ReservationFacade reservationFacade;

    @Autowired
    ReservationRepository reservationRepository;

    @Test
    @DisplayName("두 좌석에 대한 예약을 두번 시도할 경우 -> 1번은 정상 예약, 2번째는 이미 예약된 좌석 오류")
    void testReservationAndDuplicate() {
        //given
        long userId = 0;
        List<Long> reservationSeats = List.of(10L, 11L);

        //when
        var reservation = reservationFacade.reserveSeats(userId, reservationSeats);

        //then
        assertThat(reservation).isNotNull();

        //when
        ThrowableAssert.ThrowingCallable dupReservation = () -> reservationFacade.reserveSeats(userId, reservationSeats);

        //then
        assertThatThrownBy(dupReservation).hasMessage(ExceptionCode.SEAT_ALREADY_RESERVED.getMessage());
    }

    @Test
    @DisplayName("동시성 테스트: 100명이 동시에 2개의 같은 좌석을 예약할 때 1명만 성공 이후는 실패 -> Ticket은 2개만 발급")
    void testConsistent() throws InterruptedException {
        //given
        List<Long> reservationSeats = List.of(12L, 13L);

        int userCnt = 100;
        List<Long> userIdsToApply = new ArrayList<>(userCnt);
        for(long i = 0; i < userCnt; i++) {
            userIdsToApply.add(i);
        }
        ExecutorService executorService = Executors.newFixedThreadPool(userCnt);
        CountDownLatch latch = new CountDownLatch(userCnt);


        //when: 100번 동시에 예약할 때
        for (Long userId : userIdsToApply) {
            executorService.submit(() -> {
                try{ reservationFacade.reserveSeats(userId, reservationSeats); }
                catch(CustomBadRequestException e) {
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        //then: 1회의 예약 제외 모두 실패 -> ReservationTicket 2개만 발급
        var reservationTickets = reservationRepository.getCompletedOrReservedUnder5mins(reservationSeats);
        assertThat(reservationTickets.size()).isEqualTo(reservationSeats.size());
    }
}
