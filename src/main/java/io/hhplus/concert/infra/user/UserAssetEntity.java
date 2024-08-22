package io.hhplus.concert.infra.user;

import io.hhplus.concert.domain.user.UserAsset;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user_asset")
@NoArgsConstructor
public class UserAssetEntity {
    @Id
    Long userId;

    @Column(name = "balance")
    long balance;

    public UserAssetEntity(UserAsset domain) {
        this.userId = domain.getUserId();
        this.balance = domain.getBalance();
    }

    public UserAsset toDomain() {
        return new UserAsset(this.userId, this.balance);
    }
}
