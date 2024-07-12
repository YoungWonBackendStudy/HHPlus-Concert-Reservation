package io.hhplus.concert.presentation.concert;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.hhplus.concert.presentation.concert.dto.ConcertDto;
import io.hhplus.concert.presentation.concert.dto.ConcertScheduleDto;
import io.hhplus.concert.presentation.concert.dto.ConcertScheduleSeatDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "콘서트 정보")
@RestController
public class ConcertController {

    @Operation(summary = "콘서트 목록 조회 API")
    @Parameters(value = {
        @Parameter(name = "token", required = true, description = "ACTIVE상태의 토큰")
    })
    @GetMapping("concerts")
    public List<ConcertDto.Response> getConcerts(
       ConcertDto.Request concertRequest 
    ) {
        return List.of(new ConcertDto.Response(0l, "아이유 콘서트", "2024.07.05 ~ 2024.07.06 아이유 콘서트"));
    }

    @Operation(summary = "콘서트 스케줄 조회 API")
    @Parameters(value = {
        @Parameter(name = "token", required = true, description = "ACTIVE상태의 토큰"),
        @Parameter(name = "concertScheduleId", required = true, description = "조회할 콘서트의 ID")
    })
    @GetMapping("concerts/schedules")
    public List<ConcertScheduleDto.Response> concertSchedules(
        ConcertScheduleDto.Request concertScheduleRequest
    ) {
        return List.of(new ConcertScheduleDto.Response(0l, new Date(), "서울 잠실"));
    }

    @Operation(summary = "콘서트 좌석 조회 API")
    @Parameters(value = {
        @Parameter(name = "token", required = true, description = "ACTIVE상태의 토큰"),
        @Parameter(name = "concertScheduleId", required = true, description = "조회할 콘서트 스케줄의 ID")
    })
    @GetMapping("concerts/schedules/seats")
    public List<ConcertScheduleSeatDto.Response> concertScheduleSeats(
        ConcertScheduleSeatDto.Request concertSeatRequest
    ) {
        return List.of(new ConcertScheduleSeatDto.Response(0l, "R1", 120000l));
    }
}
