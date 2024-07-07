package io.hhplus.concert_reservation.controller.order.dto;

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
