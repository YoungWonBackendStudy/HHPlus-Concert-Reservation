package io.hhplus.concert.controller.payment.dto;

public class AssetGetDto {
    public static record Response(
        long balance
    ) {
    }
}
