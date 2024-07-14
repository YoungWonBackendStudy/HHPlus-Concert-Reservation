package io.hhplus.concert.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.hhplus.concert.facade.user.UserAssetFacade;

@SpringBootTest
public class UserAssetFacadeIntegTest {
    UserAssetFacade userAssetFacade;

    public UserAssetFacadeIntegTest(UserAssetFacade userAssetFacade) {
        this.userAssetFacade = userAssetFacade;
    }

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
