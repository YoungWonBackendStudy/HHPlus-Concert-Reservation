package io.hhplus.concert.infra.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAssetJpaRepository extends JpaRepository<UserAssetEntity, Long> {
    
}
