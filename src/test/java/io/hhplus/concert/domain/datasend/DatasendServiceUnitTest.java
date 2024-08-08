package io.hhplus.concert.domain.datasend;

import io.hhplus.concert.domain.payment.Payment;
import io.hhplus.concert.domain.reservation.Reservation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DatasendServiceUnitTest {
    DatasendService service;
    DatasendApiClient mockApiClient;

    public DatasendServiceUnitTest() {
        this.mockApiClient = mock(DatasendApiClient.class);
        this.service = new DatasendService(mockApiClient);
    }

    @Test
    @DisplayName("결제 내역 전송 테스트")
    void testSendPayment() throws InterruptedException {
        //given
        Reservation reservation = new Reservation(0L, 0L, Reservation.ReservationStatus.RESERVED, new Date(), null, List.of());
        Payment testPayment = new Payment(reservation);

        //when
        service.sendPayment(testPayment);

        //then
        verify(mockApiClient).sendPaymentInfo(argThat((argPayment) -> {
            assertThat(argPayment.getId()).isEqualTo(testPayment.getId());
            assertThat(argPayment.getPaidAt()).isEqualTo(testPayment.getPaidAt());
            return true;
        }));
    }
}
