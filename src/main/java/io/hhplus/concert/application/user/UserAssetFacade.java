package io.hhplus.concert.application.user;

import org.springframework.stereotype.Component;

import io.hhplus.concert.domain.user.UserAssetService;
@Component
public class UserAssetFacade {
    UserAssetService userAssetService;

    public UserAssetFacade(UserAssetService userAssetService) {
        this.userAssetService = userAssetService;
    }

    public Long getBalance(long userId) {
        return userAssetService.getUserAsset(userId).getBalance();
    }


    public Long chargeBalance(long userId, long amount) {
        return userAssetService.chargeUserAsset(userId, amount).getBalance();
    }
}
