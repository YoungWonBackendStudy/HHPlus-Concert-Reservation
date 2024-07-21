package io.hhplus.concert.infra.user;

import org.springframework.stereotype.Repository;

import io.hhplus.concert.domain.user.UserAsset;
import io.hhplus.concert.domain.user.UserAssetRepository;
import io.hhplus.concert.support.exception.CustomNotFoundException;
import io.hhplus.concert.support.exception.ExceptionCode;
import jakarta.transaction.Transactional;

@Repository
public class UserAssetRepositoryImpl implements UserAssetRepository{
    UserAssetJpaRepository userAssetJpaRepository;

    public UserAssetRepositoryImpl(UserAssetJpaRepository userAssetJpaRepository) {
        this.userAssetJpaRepository = userAssetJpaRepository;
    }

    @Override
    @Transactional
    public UserAsset getAndLockByUserId(long userId) {
        var entity = this.userAssetJpaRepository.findAndLockByUserId(userId);
        if(entity.isEmpty()) throw new CustomNotFoundException(ExceptionCode.USER_ASSET_NOT_FOUND);

        return entity.get().toDomain();
    }

    @Override
    @Transactional
    public UserAsset save(UserAsset userAsset) {
        var entity = new UserAssetEntity(userAsset);
        return this.userAssetJpaRepository.save(entity).toDomain();
    }
}
