package io.hhplus.concert.application.datasend;

import io.hhplus.concert.domain.datasend.DatasendService;
import io.hhplus.concert.domain.payment.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatasendFacade {
    private final DatasendService datasendService;

    public boolean sendPayment(Payment payment){
        return datasendService.sendPayment(payment);
    }
}
