package io.hhplus.concert.domain.user;

public interface UserAssetRepository {
    UserAsset getByUserId(long userId);
    UserAsset save(UserAsset userAsset);
}
