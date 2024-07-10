package io.hhplus.concert.controller.concert;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.hhplus.concert.controller.concert.dto.ConcertDto;
import io.hhplus.concert.controller.concert.dto.ConcertScheduleDto;
import io.hhplus.concert.controller.concert.dto.ConcertScheduleSeatDto;

@RestController
public class ConcertController {
    @GetMapping("concerts")
    public List<ConcertDto.Response> getConcerts(
       ConcertDto.Request concertRequest 
    ) {
        return List.of(new ConcertDto.Response(0l, "아이유 콘서트", "2024.07.05 ~ 2024.07.06 아이유 콘서트"));
    }

    @GetMapping("concerts/schedules")
    public List<ConcertScheduleDto.Response> concertSchedules(
        ConcertScheduleDto.Request concertScheduleRequest
    ) {
        return List.of(new ConcertScheduleDto.Response(0l, new Date(), "서울 잠실"));
    }

    @GetMapping("concerts/schedules/seats")
    public List<ConcertScheduleSeatDto.Response> concertScheduleSeats(
        ConcertScheduleSeatDto.Request concertSeatRequest
    ) {
        return List.of(new ConcertScheduleSeatDto.Response(0l, "R1", 120000l));
    }
}
