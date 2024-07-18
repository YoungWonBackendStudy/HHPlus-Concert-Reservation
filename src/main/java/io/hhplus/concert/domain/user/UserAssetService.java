package io.hhplus.concert.domain.user;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class UserAssetService {
    UserAssetRepository userAssetRepository;

    public UserAssetService(UserAssetRepository userAssetRepository) {
        this.userAssetRepository = userAssetRepository;
    }

    public UserAsset getUserAsset(long userId) {
        return userAssetRepository.getByUserId(userId);
    }

    @Transactional
    public UserAsset chargeUserAsset(long userId, long amount) {
        UserAsset userAsset = userAssetRepository.getByUserId(userId);
        userAsset.charge(amount);
        userAssetRepository.save(userAsset);
        return userAsset;
    }

    public UserAsset useUserAsset(long userId, long amount) {
        UserAsset userAsset = userAssetRepository.getByUserId(userId);
        userAsset.use(amount);
        userAssetRepository.save(userAsset);
        return userAsset;
    }
}
