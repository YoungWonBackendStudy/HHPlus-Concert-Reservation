package io.hhplus.concert.interfaces.presentation.payment;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.hhplus.concert.interfaces.presentation.payment.dto.PaymentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "결제")
@RestController
public class PaymentController {
    @Operation(summary = "결제 API")
    @Parameters(value = {
        @Parameter(name = "token", required = true, description = "ACTIVE상태의 토큰"),
        @Parameter(name = "reservationId", required = true, description = "결제할 예약 정보의 ID")
    })
    @PostMapping("pay")
    public PaymentDto.Response placePayment(
        @RequestBody PaymentDto.Request paymentRequest
    ) {
        return new PaymentDto.Response(120000, 480000);
    }
}
