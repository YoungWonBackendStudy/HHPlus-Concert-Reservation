package io.hhplus.concert.presentation.payment.dto;

public class AssetChargeDto {
    public static record Request(
        long amount
    ) {
    }

    public static record Response(
        long balance
    ) {
    }
}
