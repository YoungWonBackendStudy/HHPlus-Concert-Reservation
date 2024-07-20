package io.hhplus.concert.queue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import io.hhplus.concert.application.queue.WaitingQueueDto;
import io.hhplus.concert.application.queue.QueueFacade;
import io.hhplus.concert.support.exception.CustomBadRequestException;
import io.hhplus.concert.support.exception.ExceptionCode;

@SpringBootTest
@Sql(scripts = "classpath:testinit.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class QueueFacadeIntegTest {
    @Autowired
    QueueFacade queueFacade;

    @Test
    @DisplayName("토큰 발급, Activation 통합 테스트")
    void testActivateToken() {
        //given
        long userId = 0;

        //when
        WaitingQueueDto queueInfo = queueFacade.getQueueToken(userId);

        //then
        assertThat(queueInfo).isNotNull();
        assertThat(queueInfo.getToken()).isNotBlank();
        
        //when
        queueFacade.scheduleWaitingQueue();
        ThrowableAssert.ThrowingCallable resActivated = () -> queueFacade.getQueueToken(userId);

        //then
        assertThatThrownBy(resActivated)
            .isInstanceOf(CustomBadRequestException.class)
            .hasMessage(ExceptionCode.TOKEN_NOT_WAITING.getMessage());
    }
    
}
