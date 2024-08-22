package io.hhplus.concert.interfaces.presentation.queue.dto;

import io.hhplus.concert.application.queue.WaitingQueueDto;

public record WaitingQueueResponse(
    String token,
    long waitingAhead
) {
    public static WaitingQueueResponse of(WaitingQueueDto facadeDto) {
        return new WaitingQueueResponse(facadeDto.getToken(), facadeDto.getWaitingAhead());
    }
}
