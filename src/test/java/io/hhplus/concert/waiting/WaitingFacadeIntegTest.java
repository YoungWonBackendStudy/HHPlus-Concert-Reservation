package io.hhplus.concert.waiting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import io.hhplus.concert.application.waiting.WaitingDto;
import io.hhplus.concert.application.waiting.WaitingFacade;
import io.hhplus.concert.support.exception.CustomBadRequestException;
import io.hhplus.concert.support.exception.ExceptionCode;

@SpringBootTest
@Sql(scripts = "classpath:testinit.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class WaitingFacadeIntegTest {
    @Autowired
    WaitingFacade waitingFacade;

    @Test
    @DisplayName("토큰 발급, Activation 통합 테스트")
    void testActivateToken() {
        //given
        long userId = 0;

        //when
        WaitingDto waitingInfo = waitingFacade.getWaitingToken(userId);

        //then
        assertThat(waitingInfo).isNotNull();
        assertThat(waitingInfo.getToken()).isNotBlank();
        
        //when
        waitingFacade.scheduleWaiting();
        ThrowableAssert.ThrowingCallable resActivated = () -> waitingFacade.getWaitingToken(userId);

        //then
        assertThatThrownBy(resActivated)
            .isInstanceOf(CustomBadRequestException.class)
            .hasMessage(ExceptionCode.WAITING_TOKEN_NOT_WAITING.getMessage());
    }
    
}
