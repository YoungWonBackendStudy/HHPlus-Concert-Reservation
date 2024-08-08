package io.hhplus.concert.application.reservation;

import groovy.util.logging.Slf4j;
import io.hhplus.concert.domain.reservation.Reservation;
import io.hhplus.concert.infra.reservation.ReservationJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Calendar;
import java.util.Date;

@SpringBootTest
@ActiveProfiles("query-test")
@Slf4j
public class ReservationQueryIntegTest {
    private static final Logger log = LoggerFactory.getLogger(ReservationQueryIntegTest.class);
    @Autowired
    private ReservationJpaRepository reservationJpaRepository;

    @Test
    @DisplayName("만료예약 조회 쿼리 성능 테스트")
    void testGetReservedOver5minsAndNotCompleted() {
        //given
        var calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.AUGUST, 9, 1, 48);
        var fixedDate = calendar.getTime();

        //when
        var timeBefore = System.currentTimeMillis();
        reservationJpaRepository.findByStatusAndReservedAtBefore(Reservation.ReservationStatus.RESERVED, fixedDate);

        //then
        log.info("만료 예약 조회: {} ms", System.currentTimeMillis() - timeBefore);
    }
}
