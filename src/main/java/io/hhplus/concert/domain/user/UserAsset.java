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
        if(amount < 0) throw new CustomBadRequestException(ExceptionCode.CHARGE_AMOUNT_CANNOT_BE_NEGATIVE);

        this.balance += amount;
    }

    public void useAsset(long amount) {
        if(amount < 0) throw new CustomBadRequestException(ExceptionCode.PAYMENT_AMOUNT_CANNOT_BE_NEGATIVE);
        if(this.balance < amount) throw new CustomBadRequestException(ExceptionCode.PAYMENT_NOT_ENOUGH_BALANCE);

        this.balance -= amount;
    }
}
