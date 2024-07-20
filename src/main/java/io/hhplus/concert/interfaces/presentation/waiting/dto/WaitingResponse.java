package io.hhplus.concert.interfaces.presentation.waiting.dto;

import io.hhplus.concert.application.waiting.WaitingDto;

public record WaitingResponse(
    String waitingToken,
    long waitingId,
    long waitingAhead
) {
    public static WaitingResponse of(WaitingDto facadeDto) {
        return new WaitingResponse(facadeDto.getToken(), facadeDto.getWaitingId(), facadeDto.getWaitingAhead());
    }
}
