package io.hhplus.concert.interfaces.presentation.payment;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.hhplus.concert.application.payment.PaymentFacade;
import io.hhplus.concert.interfaces.presentation.payment.dto.PaymentRequest;
import io.hhplus.concert.interfaces.presentation.payment.dto.PaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "결제")
@RestController
public class PaymentController {
    PaymentFacade paymentFacade;
    public PaymentController(PaymentFacade paymentFacade) {
        this.paymentFacade = paymentFacade;
    }

    @Operation(summary = "결제 API")
    
    @Parameters(value = {
        @Parameter(in = ParameterIn.HEADER, name = "TOKEN", required = true, description = "ACTIVE 상태의 토큰")
    })
    @PostMapping("payment")
    public PaymentResponse placePayment(    
        @RequestHeader(name = "TOKEN") String token,
        @RequestBody PaymentRequest paymentRequest
    ) {
        return PaymentResponse.of(paymentFacade.placePayment(token, paymentRequest.reservationId()));
    }
}
