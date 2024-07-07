package io.hhplus.concert_reservation.controller.payment.dto;

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
