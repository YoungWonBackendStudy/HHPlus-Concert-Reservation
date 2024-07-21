package io.hhplus.concert.payment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.AdditionalAnswers.returnsFirstArg;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.hhplus.concert.domain.concert.ConcertSeat;
import io.hhplus.concert.domain.payment.Payment;
import io.hhplus.concert.domain.payment.PaymentRepository;
import io.hhplus.concert.domain.payment.PaymentService;
import io.hhplus.concert.domain.concert.Reservation;

public class PaymentServiceUnitTest {
    PaymentService paymentService;
    PaymentRepository mockPaymentRepository;

    public PaymentServiceUnitTest() {
        mockPaymentRepository = mock(PaymentRepository.class);
        paymentService = new PaymentService(mockPaymentRepository);
    }
    
    @Test
    @DisplayName("결제 생성 테스트")
    void testPlacePayment() {
        //given
        Reservation reservation = new Reservation(0L);
        reservation.makeTickets(List.of(new ConcertSeat(0L, 0L, "R1", 100000L, false)));
        when(mockPaymentRepository.save(any(Payment.class))).thenAnswer(returnsFirstArg());
        
        //when
        paymentService.placePayment(reservation);

        //then
        verify(mockPaymentRepository).save(any(Payment.class));
    }
}
