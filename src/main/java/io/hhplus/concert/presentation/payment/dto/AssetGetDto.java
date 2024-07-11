package io.hhplus.concert.presentation.payment.dto;

public class AssetGetDto {
    public static record Response(
        long balance
    ) {
    }
}
