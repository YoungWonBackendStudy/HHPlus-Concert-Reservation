package io.hhplus.concert.controller.reservation;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.hhplus.concert.controller.reservation.dto.ReservationDto;

@RestController
public class ReservationController {
    @PostMapping("reservations")
    public ReservationDto.Response reservation(
        @RequestParam("passToken") String passToken,
        @RequestBody ReservationDto.Request reservationRequest
    ) {
        List<ReservationDto.ReservedSeatInfo> seats = List.of(new ReservationDto.ReservedSeatInfo(0, "R1", 120000));
        return new ReservationDto.Response(0, 0, 120000, new Date(), new Date(System.currentTimeMillis() + 1000l * 60 * 5), seats);
    }
}
