package io.hhplus.concert.domain.datasend;

import io.hhplus.concert.domain.payment.Payment;

public interface DatasendApiClient {
    void sendPaymentInfo(Payment payment) throws InterruptedException;
}
