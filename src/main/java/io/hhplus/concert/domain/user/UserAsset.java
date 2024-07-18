package io.hhplus.concert.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAsset {
    Long userId;
    long balance;

    public void charge(long amount) {
        this.balance += amount;
    }

    public void use(long amount) {
        if(this.balance < amount) throw new RuntimeException("잔액이 부족합니다.");

        this.balance -= amount;
    }
}
