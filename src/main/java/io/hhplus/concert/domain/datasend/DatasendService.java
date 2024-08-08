package io.hhplus.concert.domain.datasend;

import io.hhplus.concert.domain.payment.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatasendService {
    private final DatasendApiClient datasendApiClient;

    public boolean sendPayment(Payment payment) {
        try {
            this.datasendApiClient.sendPaymentInfo(payment);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }
}
