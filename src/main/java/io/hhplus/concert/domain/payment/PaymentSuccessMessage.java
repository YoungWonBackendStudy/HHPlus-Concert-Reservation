package io.hhplus.concert.domain.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.concert.support.exception.CustomBadRequestException;
import io.hhplus.concert.support.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class PaymentSuccessMessage {
    public static final int RESEND_THRESHOLD = 3;

    private String id;
    private Long paymentId;
    private int resendCnt;
    private String payload;
    private Long producedAt;
    private PaymentMessageStatus status;

    public PaymentSuccessMessage(PaymentSuccessEvent paymentSuccessEvent) throws JsonProcessingException {
        this.id = UUID.randomUUID().toString();
        this.paymentId = paymentSuccessEvent.payment().getId();
        this.resendCnt = 0;
        this.status = PaymentMessageStatus.INIT;
        this.producedAt = System.currentTimeMillis();

        ObjectMapper objectMapper = new ObjectMapper();
        this.payload = objectMapper.writeValueAsString(paymentSuccessEvent);
    }

    public void messageResent() {
        this.resendCnt++;

        if(this.resendCnt >= RESEND_THRESHOLD) {
            this.status = PaymentMessageStatus.SKIPPED;
            throw new CustomBadRequestException(ExceptionCode.MESSAGE_RESENT_THRESHOLD);
        }
    }

    public void published() {
        this.status = PaymentMessageStatus.PUBLISHED;
    }
}
