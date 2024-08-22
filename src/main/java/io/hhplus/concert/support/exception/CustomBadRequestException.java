package io.hhplus.concert.support.exception;

import lombok.Getter;

@Getter
public class CustomBadRequestException extends RuntimeException {
    ExceptionCode code;

    public CustomBadRequestException(ExceptionCode code) {
        super(code.message);
        this.code = code;
    }
}
