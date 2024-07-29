package io.hhplus.concert.application.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(scripts = "classpath:testinit.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserAssetFacadeIntegTest {
    @Autowired
    UserAssetFacade userAssetFacade;

    @Test
    @DisplayName("잔액 조회 > 100000원 충전 > 조회 통합 테스트")
    void testChargeBalance() {
        //given
        long userId = 0;
        long chargeAmount = 100000;

        //when
        long balanceBefore = userAssetFacade.getBalance(userId);
        long balanceCharge = userAssetFacade.chargeBalance(userId, chargeAmount);
        long balanceAfter = userAssetFacade.getBalance(userId);

        //then
        assertThat(balanceCharge).isEqualTo(balanceBefore + chargeAmount);
        assertThat(balanceAfter).isEqualTo(balanceBefore + chargeAmount);
    }

    @Test
    @DisplayName("동시성 테스트: 10000원을 10번 동시에 충전할 경우 100000원 충전")
    void testChargeBalanceConsistent() throws InterruptedException {
        //given
        long userId = 0;
        long chargeAmount = 10000;
        int executionCnt = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(executionCnt);
        CountDownLatch latch = new CountDownLatch(executionCnt);

        //when
        long balanceBefore = userAssetFacade.getBalance(userId);
        for(int i = 0; i < executionCnt; i++) {
            executorService.submit(() -> {
                try{ userAssetFacade.chargeBalance(userId, chargeAmount); }
                finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();
        long balanceAfter = userAssetFacade.getBalance(userId);

        //then
        assertThat(balanceAfter).isEqualTo(chargeAmount * executionCnt + balanceBefore);
    }
    
}
