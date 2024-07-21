package io.hhplus.concert.queue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import io.hhplus.concert.support.exception.CustomNotFoundException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.hhplus.concert.domain.queue.TokenService;
import io.hhplus.concert.domain.queue.QueueToken;
import io.hhplus.concert.domain.queue.QueueToken.TokenStatus;
import io.hhplus.concert.support.exception.ExceptionCode;
import io.hhplus.concert.domain.queue.QueueTokenRepository;

public class TokenServiceUnitTest {
    TokenService tokenService;
    QueueTokenRepository mockQueueTokenRepository;
    
    public TokenServiceUnitTest() {
        this.mockQueueTokenRepository = mock(QueueTokenRepository.class);
        this.tokenService = new TokenService(this.mockQueueTokenRepository);
    }

    @Test
    @DisplayName("토큰 발급 성공 테스트")
    void testGetQueueToken() {
        //given
        long userId = 0;
        when(mockQueueTokenRepository.getActiveTokenByUserId(anyLong()))
                .thenThrow(new CustomNotFoundException(ExceptionCode.TOKEN_NOT_FOUND));
        when(mockQueueTokenRepository.saveToken(any(QueueToken.class))).thenAnswer(returnsFirstArg());

        //when
        var token = this.tokenService.validateAndGetWaitingToken(userId);

        //then
        assertThat(token).isNotNull();
        assertThat(token.getUserId()).isEqualTo(userId);
        assertThat(token.getStatus()).isEqualTo(TokenStatus.WAITING);
    }
    
    @Test
    @DisplayName("토큰 ACTIVE 검증 성공 테스트")
    void testValidateActiveToken() {
        //given
        QueueToken testToken = new QueueToken(0);
        testToken.activate();
        when(mockQueueTokenRepository.getTokenByTokenString(testToken.getToken())).thenReturn(testToken);

        //when
        ThrowableAssert.ThrowingCallable result = () -> this.tokenService.validateAndGetActiveToken(testToken.getToken());

        //then
        assertThatCode(result).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("토큰 만료 테스트")
    void testExpireTokens(){
        //given
        List<QueueToken> tokensToExpire = List.of(new QueueToken(0));
        when(mockQueueTokenRepository.getActiveTokensActivatedAtBefore(any())).thenReturn(tokensToExpire);

        //when
        tokenService.expireTokens();

        //then
        verify(mockQueueTokenRepository).saveAllTokens(anyList());
    }

    @Test
    @DisplayName("WAITING 상태 토큰 검증 실패")
    void testValidateActiveWithWaitingToken() {
        //given
        QueueToken testToken = new QueueToken(0);
        when(mockQueueTokenRepository.getTokenByTokenString(testToken.getToken())).thenReturn(testToken);

        //when
        ThrowableAssert.ThrowingCallable result = () -> this.tokenService.validateAndGetActiveToken(testToken.getToken());

        //then
        assertThatThrownBy(result).hasMessage(ExceptionCode.TOKEN_NOT_ACTIVATED.getMessage());
    }

    @Test
    @DisplayName("만료 상태 토큰 검증 실패")
    void testValidateActiveWithExpiredToken() {
        //given
        QueueToken testToken = new QueueToken(0);
        testToken.expire();
        when(mockQueueTokenRepository.getTokenByTokenString(testToken.getToken())).thenReturn(testToken);

        //when
        ThrowableAssert.ThrowingCallable result = () -> this.tokenService.validateAndGetActiveToken(testToken.getToken());

        //then
        assertThatThrownBy(result).hasMessage(ExceptionCode.TOKEN_EXPIRED.getMessage());
    }
}