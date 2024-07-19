package io.hhplus.concert.support.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    WAITING_TOKEN_NOT_FOUND("토큰을 찾을 수 없습니다."),
    WAITING_TOKEN_NOT_WAITING("토큰이 대기 상태가 아닙니다."),
    WAITING_TOKEN_NOT_ACTIVATED("토큰이 활성화 상태가 아닙니다."),
    WAITING_TOKEN_EXPIRED("만료된 토큰입니다."),

    CONCERT_NOT_FOUND("콘서트 정보를 찾을 수 없습니다."),
    CONCERT_SCHEDULE_NOT_FOUND("콘서트 스케줄 정보를 찾을 수 없습니다."),

    RESERVATION_NOT_FOUND("예약 정보를 찾을 수 없습니다."),
    RESERVATION_ALREADY_RESERVED("이미 예약된 좌석입니다."),

    USER_ASSET_NOT_FOUND("사용자 자산 정보를 찾을 수 없습니다."),

    PAYMENT_NOT_ENOUGH_BALANCE("잔액이 부족합니다."),
    PAYMENT_ALREADY_COMPLETED("이미 처리 완료된 예약입니다."),
    RESERVATION_EXPIRED("만료된 예약입니다.");
    
    String message;
    ExceptionCode(String message) {
        this.message = message;
    }
}
