package io.hhplus.concert.interfaces.presentation.concert.dto;

import java.util.Date;

import io.hhplus.concert.application.concert.ConcertScheduleDto;

public record ConcertScheduleResponse(
    long id,
    Date date,
    String place
) {
    public static ConcertScheduleResponse of(ConcertScheduleDto facadeDto) {
        return new ConcertScheduleResponse(facadeDto.getId(), facadeDto.getConcertDate(), facadeDto.getPlace());
    }
}
