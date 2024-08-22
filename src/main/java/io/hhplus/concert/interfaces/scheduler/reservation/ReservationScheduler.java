package io.hhplus.concert.interfaces.scheduler.reservation;

import io.hhplus.concert.application.reservation.ReservationFacade;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReservationScheduler {
    private final ReservationFacade reservationFacade;

    public ReservationScheduler(ReservationFacade reservationFacade) {
        this.reservationFacade = reservationFacade;
    }

    @Scheduled(fixedDelay = 1000)
    void scheduleReservationExpiration() {
        this.reservationFacade.updateSeatsExpired();
    }
}
