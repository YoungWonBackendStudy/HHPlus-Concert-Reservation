package io.hhplus.concert.interfaces.presentation.queue;


import io.hhplus.concert.application.queue.QueueFacade;
import io.hhplus.concert.interfaces.presentation.queue.dto.WaitingQueueResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "대기열")
@RestController
public class WaitingQueueController {
    QueueFacade queueFacade;
    
    public WaitingQueueController(QueueFacade queueFacade) {
        this.queueFacade = queueFacade;
    }

    @Operation(summary = "대기열 토큰 API", description = "토큰이 없을 경우 토큰 발급 / 있을 경우 기존 토큰 반환")
    @Parameters(value = {
        @Parameter(in = ParameterIn.HEADER, name = "TOKEN", description = "토큰이 있는 경우 토큰 추가")
    })
    @GetMapping("queue/token")
    public WaitingQueueResponse getWaitingInfo(
        @RequestHeader(value = "TOKEN", required = false) String token
    ) {
        return WaitingQueueResponse.of(queueFacade.getQueueToken(token));
    }
}
