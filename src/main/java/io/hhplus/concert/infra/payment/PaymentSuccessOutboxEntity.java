package io.hhplus.concert.infra.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.concert.domain.payment.PaymentMessageStatus;
import io.hhplus.concert.domain.payment.PaymentSuccessMessage;
import io.hhplus.concert.domain.payment.PaymentSuccessEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Entity
@Table(name = "payment_success_outbox")
@NoArgsConstructor
public class PaymentSuccessOutboxEntity {
    @Id
    String id;
    @Column(unique = true)
    Long paymentId;
    int resendCnt;
    PaymentMessageStatus status;
    String messageJson;
    Date producedAt;

    public PaymentSuccessOutboxEntity(PaymentSuccessMessage paymentSuccessMessage) throws JsonProcessingException {
        this.id = paymentSuccessMessage.getId();
        this.paymentId = paymentSuccessMessage.getPaymentId();
        this.resendCnt = paymentSuccessMessage.getResendCnt();
        this.status= paymentSuccessMessage.getStatus();
        this.messageJson = paymentSuccessMessage.getMessage();
        this.producedAt = new Date(paymentSuccessMessage.getProducedAt());
    }

    public PaymentSuccessMessage toDomain() throws JsonProcessingException {
        return new PaymentSuccessMessage(this.id, this.paymentId, this.resendCnt,  this.messageJson, this.producedAt.getTime(), this.status);
    }
}
