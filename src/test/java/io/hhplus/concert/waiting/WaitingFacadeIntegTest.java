package io.hhplus.concert.waiting;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.hhplus.concert.application.waiting.WaitingDto;
import io.hhplus.concert.application.waiting.WaitingFacade;
import io.hhplus.concert.domain.waiting.WaitingToken;
import io.hhplus.concert.domain.waiting.WaitingToken.TokenStatus;

@SpringBootTest
public class WaitingFacadeIntegTest {
    WaitingFacade waitingFacade;

    public WaitingFacadeIntegTest(WaitingFacade waitingFacade) {
        this.waitingFacade = waitingFacade;
    }

    @Test
    @DisplayName("토큰 발급, Activation, 만료 통합 테스트")
    void testActivateToken() {
        //given
        long userId = 0;

        //when
        WaitingDto waitingInfo = waitingFacade.getWaitingToken(userId);

        //then
        assertThat(waitingInfo).isNotNull();
        assertThat(waitingInfo.getToken()).isNotBlank();
        assertThat(waitingInfo.getStatus()).isEqualTo(TokenStatus.WAITING.name());
        assertThat(waitingInfo.getWaitingAhead()).isZero();
        
        //when
        waitingFacade.scheduleWaiting();
        waitingInfo = waitingFacade.getWaitingToken(userId);

        //then
        assertThat(waitingInfo).isNotNull();
        assertThat(waitingInfo.getToken()).isNotBlank();
        assertThat(waitingInfo.getStatus()).isEqualTo(TokenStatus.ACTIVE.name());

        //when
        Clock constantClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        Clock.offset(constantClock, Duration.ofMillis(30 * 60 * 1000l));
        
        //when
        waitingFacade.scheduleWaiting();
        waitingInfo = waitingFacade.getWaitingToken(userId);

        //then
        assertThat(waitingInfo).isNotNull();
        assertThat(waitingInfo.getToken()).isNotBlank();
        assertThat(waitingInfo.getStatus()).isEqualTo(TokenStatus.EXPIRED.name());
    }
    
}
