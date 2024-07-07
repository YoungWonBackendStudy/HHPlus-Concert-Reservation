package io.hhplus.concert_reservation.controller.payment;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.hhplus.concert_reservation.controller.payment.dto.PaymentDto;

@RestController
public class PaymentController {
    
    @PostMapping("pay")
    public PaymentDto.Response placePayment(
        @RequestBody PaymentDto.Request paymentRequest
    ) {
        return new PaymentDto.Response(120000, 480000);
    }
}
