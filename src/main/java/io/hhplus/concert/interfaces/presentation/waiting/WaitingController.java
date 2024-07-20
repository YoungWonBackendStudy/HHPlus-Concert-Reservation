package io.hhplus.concert.interfaces.presentation.waiting;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.hhplus.concert.application.waiting.WaitingFacade;
import io.hhplus.concert.interfaces.presentation.waiting.dto.WaitingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "대기열 토큰")
@RestController
public class WaitingController {
    WaitingFacade waitingFacade;
    
    public WaitingController(WaitingFacade waitingFacade) {
        this.waitingFacade = waitingFacade;
    }

    @Operation(summary = "대기열 토큰 API", description = "토큰이 없을 경우 토큰 발급 / 있을 경우 기존 토큰 반환")
    @Parameters(value = {
        @Parameter(name = "userId", required = true, description = "대기열 진입/확인할 사용자의 ID")
    })
    @GetMapping("waiting/token")
    public WaitingResponse getWaitingInfo(
        @RequestParam("userId") long userId
    ) {
        return WaitingResponse.of(waitingFacade.getWaitingToken(userId));
    }
}
