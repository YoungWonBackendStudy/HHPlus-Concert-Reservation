package io.hhplus.concert.domain.payment;

import io.hhplus.concert.domain.concert.ConcertSeat;
import io.hhplus.concert.domain.reservation.Reservation;
import io.hhplus.concert.support.exception.ExceptionCode;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PaymentServiceUnitTest {
    PaymentService paymentService;
    PaymentRepository mockPaymentRepository;
    PaymentEventPublisher mockPaymentEventPublisher;

    public PaymentServiceUnitTest() {
        mockPaymentRepository = mock(PaymentRepository.class);
        mockPaymentEventPublisher = mock(PaymentEventPublisher.class);
        paymentService = new PaymentService(mockPaymentRepository, mockPaymentEventPublisher);
    }
    
    @Test
    @DisplayName("결제 성공")
    void testPlacePayment() {
        //given
        Reservation reservation = new Reservation(0L, 0L, Reservation.ReservationStatus.RESERVED, new Date(), null, List.of());
        reservation.makeTickets(List.of(new ConcertSeat(0L, 0L, "R1", 100000L, false)));
        when(mockPaymentRepository.save(any(Payment.class))).thenAnswer(returnsFirstArg());
        
        //when
        paymentService.placePayment(reservation);

        //then
        verify(mockPaymentRepository).save(any(Payment.class));
    }

    @Test
    @DisplayName("결제 완료 이벤트 전송 성공")
    void testPaymentCompleted() {
        //given
        String token = "testToken";
        Reservation reservation = new Reservation(0L, 0L, Reservation.ReservationStatus.RESERVED, new Date(), null, List.of());
        Payment testPayment = new Payment(reservation);
        //when
        paymentService.paymentCompleted(token, testPayment);

        //then
        verify(mockPaymentEventPublisher).publishPaymentSuccessEvent(argThat((paymentEvent)-> {
            assertThat(paymentEvent.token()).isEqualTo(token);
            assertThat(paymentEvent.payment().getId()).isEqualTo(testPayment.getId());
            assertThat(paymentEvent.payment().getPaidAt()).isEqualTo(testPayment.getPaidAt());
            return true;
        }));
    }


    @Test
    @DisplayName("5분이 넘은 예약을 결제하려 할 때 예약 만료 오류 발생")
    void testPlacePaymentWithExpiredReservation() {
        //given
        Reservation reservation = new Reservation(0L, 0L, Reservation.ReservationStatus.EXPIRED, new Date(System.currentTimeMillis() - 5 * 60 * 1000L - 1), null, List.of());
        reservation.makeTickets(List.of(new ConcertSeat(0L, 0L, "R1", 100000L, false)));
        when(mockPaymentRepository.save(any(Payment.class))).thenAnswer(returnsFirstArg());

        //when
        ThrowableAssert.ThrowingCallable throwRes = () -> paymentService.placePayment(reservation);

        //then
        assertThatThrownBy(throwRes).hasMessage(ExceptionCode.RESERVATION_EXPIRED.getMessage());
    }

    @Test
    @DisplayName("이미 완료된 예약을 결제하려 할 때 이미 처리된 예약 오류 발생")
    void testPlacePaymentWithCompletedReservation() {
        //given
        Reservation reservation = new Reservation(0L, 0L, Reservation.ReservationStatus.COMPLETED, new Date(), new Date(), List.of());
        reservation.makeTickets(List.of(new ConcertSeat(0L, 0L, "R1", 100000L, false)));
        when(mockPaymentRepository.save(any(Payment.class))).thenAnswer(returnsFirstArg());

        //when
        ThrowableAssert.ThrowingCallable throwRes = () -> paymentService.placePayment(reservation);

        //then
        assertThatThrownBy(throwRes).hasMessage(ExceptionCode.PAYMENT_ALREADY_COMPLETED.getMessage());
    }
}
