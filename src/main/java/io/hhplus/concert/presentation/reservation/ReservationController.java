package io.hhplus.concert.presentation.reservation;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.hhplus.concert.presentation.reservation.dto.ReservationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "콘서트 좌석 예약")
@RestController
public class ReservationController {

    @Operation(summary = "콘서트 좌석 예약 API")
    @Parameters(value = {
        @Parameter(name = "token", required = true, description = "ACTIVE상태의 토큰")
    })
    @PostMapping("reservations")
    public ReservationDto.Response reservation(
        @RequestParam("token") String token,
        @RequestBody ReservationDto.Request reservationRequest
    ) {
        List<ReservationDto.ReservedSeatInfo> seats = List.of(new ReservationDto.ReservedSeatInfo(0, "R1", 120000));
        return new ReservationDto.Response(0, 0, 120000, new Date(), new Date(System.currentTimeMillis() + 1000l * 60 * 5), seats);
    }
}
