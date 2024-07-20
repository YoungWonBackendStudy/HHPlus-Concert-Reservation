package io.hhplus.concert.queue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.hhplus.concert.domain.queue.QueueToken;
import io.hhplus.concert.domain.queue.QueueToken.TokenStatus;
import io.hhplus.concert.support.exception.ExceptionCode;

public class QueueTokenDomainUnitTest {
    @Test
    @DisplayName("Active상태가 아닌 토큰을 Activate하면 상태가 ACTIVE상태로 변경")
    void testActivate() {
        //given
        long userId = 0;
        QueueToken token = new QueueToken(userId);
        assertThat(token.getStatus()).isNotEqualTo(TokenStatus.ACTIVE);

        //when
        token.activate();

        //then
        assertThat(token.getStatus()).isEqualTo(TokenStatus.ACTIVE);
    }

    @Test
    @DisplayName("만료되지 않은 토큰을 Expire하면 상태가 만료 상태로 변경")
    void testExpire() {
        //given
        long userId = 0;
        QueueToken token = new QueueToken(userId);
        assertThat(token.getStatus()).isNotEqualTo(TokenStatus.EXPIRED);

        //when
        token.expire();

        //then
        assertThat(token.getStatus()).isEqualTo(TokenStatus.EXPIRED);
    }

    @Test
    @DisplayName("Waiting상태 토큰을 validateActivation하면 NOT_ACTIVATED 오류 발생")
    void testWaitingTokenValidateActivation() {
        //given
        long userId = 0;
        QueueToken token = new QueueToken(userId);
        assertThat(token.getStatus()).isEqualTo(TokenStatus.WAITING);

        //when
        ThrowableAssert.ThrowingCallable result = () -> token.validateActivation();

        //then
        assertThatThrownBy(result).hasMessage(ExceptionCode.TOKEN_NOT_ACTIVATED.getMessage());
    }

    @Test
    @DisplayName("만료된 토큰을 validateActivation하면 TOKEN_EXPIRED 오류 발생")
    void testExpiredTokenValidateActivation() {
        //given
        long userId = 0;
        QueueToken token = new QueueToken(userId);
        token.expire();
        assertThat(token.getStatus()).isEqualTo(TokenStatus.EXPIRED);

        //when
        ThrowableAssert.ThrowingCallable result = () -> token.validateActivation();

        //then
        assertThatThrownBy(result).hasMessage(ExceptionCode.TOKEN_EXPIRED.getMessage());
    }

    @Test
    @DisplayName("Active상태 토큰을 validateActivation하면 오류 없이 정상 동작")
    void testValidateActivation() {
        //given
        long userId = 0;
        QueueToken token = new QueueToken(userId);
        token.activate();
        assertThat(token.getStatus()).isEqualTo(TokenStatus.ACTIVE);

        //when
        ThrowableAssert.ThrowingCallable result = () -> token.validateActivation();

        //then
        assertThatCode(result).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("ACTIVE상태 토큰을 validateWaiting하면 NOT_WAITING 오류 발생")
    void testValidateWaiting() {
        //given
        long userId = 0;
        QueueToken token = new QueueToken(userId);
        token.activate();
        assertThat(token.getStatus()).isEqualTo(TokenStatus.ACTIVE);

        //when
        ThrowableAssert.ThrowingCallable result = () -> token.validateWaiting();

        //then
        assertThatThrownBy(result).hasMessage(ExceptionCode.TOKEN_NOT_WAITING.getMessage());
    }

    @Test
    @DisplayName("WAITING 토큰을 validateWaiting하면 오류 없이 정상 동작")
    void testActiveTokenValidateWaiting() {
        //given
        long userId = 0;
        QueueToken token = new QueueToken(userId);
        assertThat(token.getStatus()).isEqualTo(TokenStatus.WAITING);

        //when
        ThrowableAssert.ThrowingCallable result = () -> token.validateWaiting();

        //then
        assertThatCode(result).doesNotThrowAnyException();
    }
}
