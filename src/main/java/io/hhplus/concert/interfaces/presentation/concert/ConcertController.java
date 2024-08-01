package io.hhplus.concert.interfaces.presentation.concert;

import io.hhplus.concert.application.concert.ConcertFacade;
import io.hhplus.concert.interfaces.presentation.concert.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "콘서트")
@RestController
public class ConcertController {
    private final ConcertFacade concertFacade;
    public ConcertController(ConcertFacade concertFacade) {
        this.concertFacade = concertFacade;
    }

    @Operation(summary = "콘서트 목록 조회 API")
    @Parameters(value = {
        @Parameter(in = ParameterIn.HEADER, name = "TOKEN", required = true, description = "ACTIVE 상태의 토큰"),
        @Parameter(name = "token", required = true, description = "ACTIVE 상태의 토큰")
    })
    @GetMapping("concerts")
    public List<ConcertResponse> getConcerts(
    ) {
        return concertFacade.getConcerts()
            .stream().map(ConcertResponse::of)
            .toList();
    }

    @Operation(summary = "콘서트 스케줄 조회 API")
    @Parameters(value = {
        @Parameter(in = ParameterIn.HEADER, name = "TOKEN", required = true, description = "ACTIVE 상태의 토큰"),
        @Parameter(name = "concertScheduleId", required = true, description = "조회할 콘서트의 ID")
    })
    @GetMapping("concerts/schedules")
    public List<ConcertScheduleResponse> concertSchedules(
        ConcertScheduleRequest concertScheduleRequest
    ) {
        return concertFacade.getConcertSchedules(concertScheduleRequest.concertId())
            .stream().map(ConcertScheduleResponse::of)
            .toList();
    }

    @Operation(summary = "콘서트 좌석 조회 API")
    @Parameters(value = {
        @Parameter(in = ParameterIn.HEADER, name = "TOKEN", required = true, description = "ACTIVE 상태의 토큰"),
        @Parameter(name = "concertScheduleId", required = true, description = "조회할 콘서트 스케줄의 ID")
    })
    @GetMapping("concerts/seats")
    public List<ConcertSeatResponse> concertSeats(
        ConcertSeatRequest concertSeatRequest
    ) {
        return concertFacade.getConcertSeats(concertSeatRequest.concertScheduleId())
            .stream().map(ConcertSeatResponse::of)
            .toList();
    }


}
