package io.hhplus.concert.infra.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.lang.NonNull;

import jakarta.persistence.LockModeType;

public interface UserAssetJpaRepository extends JpaRepository<UserAssetEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Optional<UserAssetEntity> findAndLockByUserId(@NonNull Long userId);
}
