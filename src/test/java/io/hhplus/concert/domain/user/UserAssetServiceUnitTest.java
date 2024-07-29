package io.hhplus.concert.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.hhplus.concert.support.exception.ExceptionCode;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        UserAsset testUserAsset = new UserAsset(0L, 3000);
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
        UserAsset testUserAsset = new UserAsset(0L, 3000);
        long chargeAmount = 10000;
        when(mockUserRepository.getAndLockByUserId(testUserAsset.getUserId())).thenReturn(testUserAsset);
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
        UserAsset testUserAsset = new UserAsset(0L, 3000);
        long useAmount = 1000;
        when(mockUserRepository.getAndLockByUserId(testUserAsset.getUserId())).thenReturn(testUserAsset);
        when(mockUserRepository.save(any(UserAsset.class))).thenAnswer(returnsFirstArg());

        //when
        long beforeBalance = testUserAsset.getBalance();
        var resUserAsset = userAssetService.useUserAsset(testUserAsset.getUserId(), useAmount);

        //then
        assertThat(resUserAsset).isNotNull();
        assertThat(resUserAsset.getBalance()).isEqualTo(beforeBalance - useAmount);
    }

    @Test
    @DisplayName("0보다 적은 금액을 충전/사용하려 할 경우 오류 발생")
    void testChargeAmountNegative() {
        //given
        UserAsset testUserAsset = new UserAsset(0L, 3000);
        long chargeAmount = -1;
        long useAmount = -1;
        when(mockUserRepository.getAndLockByUserId(testUserAsset.getUserId())).thenReturn(testUserAsset);
        when(mockUserRepository.save(any(UserAsset.class))).thenAnswer(returnsFirstArg());

        //when
        ThrowableAssert.ThrowingCallable chargeRes = () -> userAssetService.chargeUserAsset(testUserAsset.getUserId(), chargeAmount);
        ThrowableAssert.ThrowingCallable useRes = () -> userAssetService.useUserAsset(testUserAsset.getUserId(), useAmount);

        //then
        assertThatThrownBy(chargeRes).hasMessage(ExceptionCode.CHARGE_AMOUNT_CANNOT_BE_NEGATIVE.getMessage());
        assertThatThrownBy(useRes).hasMessage(ExceptionCode.PAYMENT_AMOUNT_CANNOT_BE_NEGATIVE.getMessage());
    }

    @Test
    @DisplayName("3000원 잔액을 가진 사용자가 10000원 사용할 때 잔액 부족 오류")
    void testUseBalanceOver() {
        //given
        UserAsset testUserAsset = new UserAsset(0L, 3000);
        long useAmount = 10000;
        when(mockUserRepository.getAndLockByUserId(testUserAsset.getUserId())).thenReturn(testUserAsset);
        when(mockUserRepository.save(any(UserAsset.class))).thenAnswer(returnsFirstArg());

        //when
        ThrowableAssert.ThrowingCallable res = () -> userAssetService.useUserAsset(testUserAsset.getUserId(), useAmount);

        //then
        assertThatThrownBy(res).hasMessage(ExceptionCode.NOT_ENOUGH_BALANCE.getMessage());
    }

}
