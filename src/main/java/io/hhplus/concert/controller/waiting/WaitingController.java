package io.hhplus.concert.controller.waiting;


import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.hhplus.concert.controller.waiting.dto.WaitingCheckDto;
import io.hhplus.concert.controller.waiting.dto.WaitingTokenDto;

@RestController
public class WaitingController {
    @GetMapping("waiting/token")
    public WaitingTokenDto.Response getToken(
        @RequestParam("userId") long userId
    ) {
        return new WaitingTokenDto.Response("ConcertReservationWaitingToken");
    }

    @GetMapping("waiting/check")
    public WaitingCheckDto.Response passWaiting(
        @RequestParam("waitingToken") String waitingToken
    ) {
        return new WaitingCheckDto.Response(true, -1, -1, "ConcertReservationPassToken", new Date(System.currentTimeMillis() + 1000l * 60 * 60));
    }
}
