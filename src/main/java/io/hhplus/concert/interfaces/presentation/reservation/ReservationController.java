package io.hhplus.concert.interfaces.presentation.reservation;

import io.hhplus.concert.application.reservation.ReservationFacade;
import io.hhplus.concert.interfaces.presentation.reservation.dto.ReservationRequest;
import io.hhplus.concert.interfaces.presentation.reservation.dto.ReservationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {
    private final ReservationFacade reservationFacade;

    public ReservationController(ReservationFacade reservationFacade) {
        this.reservationFacade = reservationFacade;
    }

    @Operation(summary = "콘서트 좌석 예약 API")
    @Parameters(value = {
            @Parameter(in = ParameterIn.HEADER, name = "WAITING_TOKEN", required = true, description = "ACTIVE상태의 토큰"),
            @Parameter(name = "concertScheduleId", required = true, description = "예약할 콘서트 스케줄"),
            @Parameter(name = "seatIds", required = true, description = "예약할 콘서트 좌석 List"),
            @Parameter(name = "userId", hidden = true)
    })
    @PostMapping("reservations")
    public ReservationResponse reservation(
            HttpServletRequest request,
            @RequestBody ReservationRequest reservationRequest
    ) {
        long userId = (long) request.getAttribute("userId");
        var reservationResult = reservationFacade.reserveSeats(userId, reservationRequest.seatIds());
        return ReservationResponse.of(reservationResult);
    }
}
