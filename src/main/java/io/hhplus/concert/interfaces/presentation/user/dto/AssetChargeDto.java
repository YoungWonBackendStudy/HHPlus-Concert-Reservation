package io.hhplus.concert.interfaces.presentation.user.dto;

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
