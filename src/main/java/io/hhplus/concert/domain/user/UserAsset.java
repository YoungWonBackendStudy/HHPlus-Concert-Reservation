package io.hhplus.concert.domain.user;

import io.hhplus.concert.support.exception.CustomBadRequestException;
import io.hhplus.concert.support.exception.ExceptionCode;
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
        if(this.balance < amount) throw new CustomBadRequestException(ExceptionCode.PAYMENT_NOT_ENOUGH_BALANCE);

        this.balance -= amount;
    }
}
