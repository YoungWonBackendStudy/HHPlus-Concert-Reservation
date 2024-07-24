package io.hhplus.concert.support.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    TOKEN_NOT_FOUND("404","토큰을 찾을 수 없습니다."),
    CONCERT_NOT_FOUND("404","콘서트 정보를 찾을 수 없습니다."),
    CONCERT_SCHEDULE_NOT_FOUND("404","콘서트 스케줄 정보를 찾을 수 없습니다."),
    CONCERT_SEAT_NOT_FOUND("404","콘서트 좌석 정보를 찾을 수 없습니다."),
    RESERVATION_NOT_FOUND("404","예약 정보를 찾을 수 없습니다."),
    USER_ASSET_NOT_FOUND("404","사용자 자산 정보를 찾을 수 없습니다."),

    TOKEN_NOT_WAITING("400", "토큰이 대기 상태가 아닙니다."),
    TOKEN_NOT_ACTIVATED("400", "토큰이 활성화 상태가 아닙니다."),
    TOKEN_EXPIRED("400", "만료된 토큰입니다."),

    SEAT_ALREADY_RESERVED("400", "이미 예약된 좌석입니다."),
    RESERVATION_EXPIRED("400", "만료된 예약입니다."),

    CHARGE_AMOUNT_CANNOT_BE_NEGATIVE("400", "0보다 적은 금액은 충전할 수 없습니다."),

    NOT_ENOUGH_BALANCE("400", "잔액이 부족합니다."),
    PAYMENT_AMOUNT_CANNOT_BE_NEGATIVE("400", "0보다 적은 금액은 결제할 수 없습니다."),
    PAYMENT_ALREADY_COMPLETED("400", "이미 처리 완료된 예약입니다.");

    final String errorCode;
    final String message;
    ExceptionCode(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
