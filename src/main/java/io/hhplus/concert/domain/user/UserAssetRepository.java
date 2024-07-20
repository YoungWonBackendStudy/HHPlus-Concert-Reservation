package io.hhplus.concert.domain.user;

public interface UserAssetRepository {
    UserAsset getAndLockByUserId(long userId);
    UserAsset save(UserAsset userAsset);
}
