package io.hhplus.concert.support.exception;

import lombok.Getter;

@Getter
public class CustomNotFoundException extends RuntimeException {
    ExceptionCode code;

    public CustomNotFoundException(ExceptionCode code) {
        super(code.message);
        this.code = code;
    }
}
