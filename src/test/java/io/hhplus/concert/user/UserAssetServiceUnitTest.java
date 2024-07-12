package io.hhplus.concert.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.hhplus.concert.domain.user.UserAsset;
import io.hhplus.concert.domain.user.UserAssetRepository;
import io.hhplus.concert.domain.user.UserAssetService;

public class UserAssetServiceUnitTest {
    UserAssetService userAssetService;
    UserAssetRepository mockUserRepository;

    public UserAssetServiceUnitTest() {
        this.mockUserRepository = mock(UserAssetRepository.class);
        this.userAssetService = new UserAssetService(mockUserRepository);
    }

    @Test
    @DisplayName("잔액 조회 테스트")
    void testGetBalance() {
        //given
        UserAsset testUserAsset = new UserAsset(0, 3000);
        when(mockUserRepository.getByUserId(testUserAsset.getUserId())).thenReturn(testUserAsset);

        //when
        var resUserAsset = userAssetService.getUserAsset(testUserAsset.getUserId());

        //then
        assertThat(resUserAsset).isEqualTo(testUserAsset);
    }

    @Test
    @DisplayName("잔액 10000원 충전 성공 테스트")
    void testChargeBalance() {
        //given
        UserAsset testUserAsset = new UserAsset(0, 3000);
        long chargeAmount = 10000;
        when(mockUserRepository.getByUserId(testUserAsset.getUserId())).thenReturn(testUserAsset);
        when(mockUserRepository.save(any(UserAsset.class))).thenAnswer(returnsFirstArg());

        //when
        long beforeBalance = testUserAsset.getBalance();
        var resUserAsset = userAssetService.chargeUserAsset(testUserAsset.getUserId(), chargeAmount);

        //then
        assertThat(resUserAsset).isNotNull();
        assertThat(resUserAsset.getBalance()).isEqualTo(beforeBalance + chargeAmount);
    }

    @Test
    @DisplayName("잔액 1000원 사용 성공 테스트")
    void testUseBalance() {
        //given
        UserAsset testUserAsset = new UserAsset(0, 3000);
        long useAmount = 1000;
        when(mockUserRepository.getByUserId(testUserAsset.getUserId())).thenReturn(testUserAsset);
        when(mockUserRepository.save(any(UserAsset.class))).thenAnswer(returnsFirstArg());

        //when
        long beforeBalance = testUserAsset.getBalance();
        var resUserAsset = userAssetService.useUserAsset(testUserAsset.getUserId(), useAmount);

        //then
        assertThat(resUserAsset).isNotNull();
        assertThat(resUserAsset.getBalance()).isEqualTo(beforeBalance - useAmount);
    }

    @Test
    @DisplayName("잔액 부족으로 10000원 사용 불가 테스트")
    void testUseBalanceOver() {
        //given
        UserAsset testUserAsset = new UserAsset(0, 3000);
        long useAmount = 10000;
        when(mockUserRepository.getByUserId(testUserAsset.getUserId())).thenReturn(testUserAsset);
        when(mockUserRepository.save(any(UserAsset.class))).thenAnswer(returnsFirstArg());

        //when
        ThrowableAssert.ThrowingCallable res = () -> userAssetService.useUserAsset(testUserAsset.getUserId(), useAmount);

        //then
        assertThatThrownBy(res).hasMessage("잔액이 부족합니다.");
    }

}
