package io.hhplus.concert.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.hhplus.concert.application.reservation.ReservationFacade;
import io.hhplus.concert.domain.waiting.TokenService;
import io.hhplus.concert.domain.waiting.WaitingToken;

@SpringBootTest
public class ReservationFacadeIntegTest {
    ReservationFacade reservationFacade;
    TokenService tokenService;
    public ReservationFacadeIntegTest(ReservationFacade reservationFacade, TokenService tokenService) {
        this.reservationFacade = reservationFacade;
        this.tokenService = tokenService;
    }

    @Test
    @DisplayName("예약 통합 테스트")
    void testReservation() {
        //given
        long userId = 0;
        long concertScheduleId = 0;
        WaitingToken token = tokenService.getToken(userId);
        List<Long> reservatinSeats = List.of(0l, 1l);

        //when
        var reservation = reservationFacade.reserveSeats(token.getToken(), concertScheduleId, reservatinSeats);

        //then
        assertThat(reservation).isNotNull();

        //when
        ThrowableAssert.ThrowingCallable dupReservation = () -> reservationFacade.reserveSeats(token.getToken(), concertScheduleId, reservatinSeats);

        //then
        assertThatThrownBy(dupReservation).hasMessage("이미 예약된 좌석입니다.");
    }
}
