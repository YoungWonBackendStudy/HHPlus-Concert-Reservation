package io.hhplus.concert.infra.datasend;

import io.hhplus.concert.domain.datasend.DatasendApiClient;
import io.hhplus.concert.domain.payment.Payment;
import org.springframework.stereotype.Component;

@Component
public class DatasendApiClientImpl implements DatasendApiClient {
    @Override
    public void sendPaymentInfo(Payment payment) throws InterruptedException {
        Thread.sleep(2000L);
    }
}
