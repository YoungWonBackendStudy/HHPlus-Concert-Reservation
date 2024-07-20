package io.hhplus.concert.interfaces.presentation.queue;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.hhplus.concert.application.queue.QueueFacade;
import io.hhplus.concert.interfaces.presentation.queue.dto.WaitingQueueResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "대기열 토큰")
@RestController
public class WaitingQueueController {
    QueueFacade queueFacade;
    
    public WaitingQueueController(QueueFacade queueFacade) {
        this.queueFacade = queueFacade;
    }

    @Operation(summary = "대기열 토큰 API", description = "토큰이 없을 경우 토큰 발급 / 있을 경우 기존 토큰 반환")
    @Parameters(value = {
        @Parameter(name = "userId", required = true, description = "대기열 진입/확인할 사용자의 ID")
    })
    @GetMapping("queue/token")
    public WaitingQueueResponse getWaitingInfo(
        @RequestParam("userId") long userId
    ) {
        return WaitingQueueResponse.of(queueFacade.getQueueToken(userId));
    }
}
