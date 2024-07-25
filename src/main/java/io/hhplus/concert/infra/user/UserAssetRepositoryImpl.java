package io.hhplus.concert.infra.user;

import io.hhplus.concert.domain.user.UserAsset;
import io.hhplus.concert.domain.user.UserAssetRepository;
import io.hhplus.concert.support.exception.CustomNotFoundException;
import io.hhplus.concert.support.exception.ExceptionCode;
import org.springframework.stereotype.Repository;

@Repository
public class UserAssetRepositoryImpl implements UserAssetRepository{
    UserAssetJpaRepository userAssetJpaRepository;

    public UserAssetRepositoryImpl(UserAssetJpaRepository userAssetJpaRepository) {
        this.userAssetJpaRepository = userAssetJpaRepository;
    }

    @Override
    public UserAsset getByUserId(long userId) {
        var entity = this.userAssetJpaRepository.findById(userId);
        if(entity.isEmpty()) throw new CustomNotFoundException(ExceptionCode.USER_ASSET_NOT_FOUND);

        return entity.get().toDomain();
    }

    @Override
    public UserAsset getAndLockByUserId(long userId) {
        var entity = this.userAssetJpaRepository.findAndLockByUserId(userId);
        if(entity.isEmpty()) throw new CustomNotFoundException(ExceptionCode.USER_ASSET_NOT_FOUND);

        return entity.get().toDomain();
    }

    @Override
    public UserAsset save(UserAsset userAsset) {
        var entity = new UserAssetEntity(userAsset);
        return this.userAssetJpaRepository.save(entity).toDomain();
    }
}
