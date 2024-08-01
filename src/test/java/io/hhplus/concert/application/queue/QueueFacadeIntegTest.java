package io.hhplus.concert.application.queue;

import io.hhplus.concert.domain.queue.ActiveTokenRepository;
import io.hhplus.concert.domain.queue.WaitingQueueTokenRepository;
import io.hhplus.concert.support.exception.CustomBadRequestException;
import io.hhplus.concert.support.exception.ExceptionCode;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Sql(scripts = "classpath:testinit.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class QueueFacadeIntegTest {
    @Autowired
    QueueFacade queueFacade;

    @Autowired
    WaitingQueueTokenRepository queueTokenRepository;

    @Autowired
    ActiveTokenRepository activeTokenRepository;

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
            .hasMessage(ExceptionCode.TOKEN_IS_ACTIVATED.getMessage());
    }

    @Test
    @DisplayName("동일한 사용자가 토큰 10개 동시에 발급 요청했을 때 정상적으로 토큰 발급")
    void testGetTokenConsistent() throws InterruptedException {
        //given
        int userId = 0;
        int executionCnt = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(executionCnt);
        CountDownLatch latch = new CountDownLatch(executionCnt);

        //when
        for(int i = 0; i < executionCnt; i++) {
            executorService.submit(() -> {
                try{queueFacade.getQueueToken(userId); }
                finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        //then
        queueFacade.scheduleWaitingQueue();
        assertThatCode(() -> activeTokenRepository.getActiveTokenByUserId(userId)).doesNotThrowAnyException();
        var activeToken = activeTokenRepository.getActiveTokenByUserId(userId);
        assertThat(activeToken).isNotNull();
    }



    @Test
    @DisplayName("200개의 토큰이 대기중인 상태에서 토큰 만료/활성화 Scheduling이 10번 동시에 발생할 때 100개만 Activation")
    void testScheduleTokenConsistent() throws InterruptedException {
        //given
        int users = 200;
        for(int i = 0; i < users; i++) {
            queueFacade.getQueueToken(i);
        }
        int executionCnt = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(executionCnt);
        CountDownLatch latch = new CountDownLatch(executionCnt);

        //when
        Long activeTokensBefore = activeTokenRepository.getActiveTokensCount();
        for(int i = 0; i < executionCnt; i++) {
            executorService.submit(() -> {
                try{ queueFacade.scheduleWaitingQueue(); }
                finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        //then
        Long activeTokensAfter = activeTokenRepository.getActiveTokensCount();
        assertThat(activeTokensAfter - activeTokensBefore).isCloseTo(100L, within(10L));
    }

}
