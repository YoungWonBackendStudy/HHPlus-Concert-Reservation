package io.hhplus.concert.interfaces.controlleradvice;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        String code,
        String message
) {
}
