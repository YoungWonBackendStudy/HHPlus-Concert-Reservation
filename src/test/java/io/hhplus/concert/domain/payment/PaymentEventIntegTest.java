package io.hhplus.concert.domain.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.hhplus.concert.infra.payment.PaymentSuccessOutboxJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = { "payment-success" })
public class PaymentEventIntegTest {
    @Autowired
    private TestPaymentEventListener testPaymentEventListener;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PaymentSuccessOutboxJpaRepository paymentSuccessOutboxJpaRepository;

    @Test
    @DisplayName("Payment 성공 Event 비동기 발행 성공 여부 및 Message 아웃박스 PUBLISHED 변경 테스트")
    void testPaymentEventTest() throws JsonProcessingException {
        //given
        Payment testPayment = new Payment(999L, 0L, 0L, new Date());

        //when
        int eventCalledCnt = testPaymentEventListener.getSuccessEventCalledCnt();
        int messageEventCalledCnt = testPaymentEventListener.getSuccessMessageEventCalledCnt();
        paymentService.paymentCompleted("testToken", testPayment);

        //then
        await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> {
            assertThat(testPaymentEventListener.getSuccessEventCalledCnt()).isGreaterThanOrEqualTo(eventCalledCnt + 1);
            assertThat(testPaymentEventListener.getSuccessMessageEventCalledCnt()).isGreaterThanOrEqualTo(messageEventCalledCnt + 1);
            
            var paymentMessageConsumed = paymentSuccessOutboxJpaRepository.findByPaymentId(testPayment.getId());
            assertThat(paymentMessageConsumed.getStatus()).isEqualTo(PaymentMessageStatus.PUBLISHED);
        });
    }
}
