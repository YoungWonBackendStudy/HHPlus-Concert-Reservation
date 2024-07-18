package io.hhplus.concert.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import io.hhplus.concert.application.user.UserAssetFacade;

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
    
}
