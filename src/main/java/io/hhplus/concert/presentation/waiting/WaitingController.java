package io.hhplus.concert.presentation.waiting;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.hhplus.concert.presentation.waiting.dto.WaitingTokenDto;

@RestController
public class WaitingController {
    @GetMapping("waiting/token")
    public WaitingTokenDto.Response getWaitingInfo(
        @RequestParam("userId") long userId
    ) {
        return new WaitingTokenDto.Response("ConcertReservationWaitingToken", 0, 0);
    }
}
