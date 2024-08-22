package io.hhplus.concert.interfaces.controlleradvice;

public record ErrorResponse(
        String code,
        String message
) {
}
