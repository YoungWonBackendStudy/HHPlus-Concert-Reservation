package io.hhplus.concert.domain.queue;

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

import io.hhplus.concert.domain.queue.WaitingQueueToken.TokenStatus;
import io.hhplus.concert.support.exception.ExceptionCode;

public class TokenServiceUnitTest {
    TokenService tokenService;
    WaitingQueueTokenRepository mockWaitingQueueTokenRepository;
    
    public TokenServiceUnitTest() {
        this.mockWaitingQueueTokenRepository = mock(WaitingQueueTokenRepository.class);
        this.tokenService = new TokenService(this.mockWaitingQueueTokenRepository);
    }

    @Test
    @DisplayName("토큰 발급 성공 테스트")
    void testGetQueueToken() {
        //given
        long userId = 0;
        when(mockWaitingQueueTokenRepository.getActiveTokenByUserId(anyLong()))
                .thenThrow(new CustomNotFoundException(ExceptionCode.TOKEN_NOT_FOUND));
        when(mockWaitingQueueTokenRepository.saveToken(any(WaitingQueueToken.class))).thenAnswer(returnsFirstArg());

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
        WaitingQueueToken testToken = new WaitingQueueToken(0);
        testToken.activate();
        when(mockWaitingQueueTokenRepository.getTokenByTokenString(testToken.getToken())).thenReturn(testToken);

        //when
        ThrowableAssert.ThrowingCallable result = () -> this.tokenService.validateAndGetActiveToken(testToken.getToken());

        //then
        assertThatCode(result).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("토큰 만료 테스트")
    void testExpireTokens(){
        //given
        List<WaitingQueueToken> tokensToExpire = List.of(new WaitingQueueToken(0));
        when(mockWaitingQueueTokenRepository.getActiveTokensActivatedAtBefore(any())).thenReturn(tokensToExpire);

        //when
        tokenService.expireTokens();

        //then
        verify(mockWaitingQueueTokenRepository).saveTokens(anyList());
    }

    @Test
    @DisplayName("WAITING 상태 토큰 검증 실패")
    void testValidateActiveWithWaitingToken() {
        //given
        WaitingQueueToken testToken = new WaitingQueueToken(0);
        when(mockWaitingQueueTokenRepository.getTokenByTokenString(testToken.getToken())).thenReturn(testToken);

        //when
        ThrowableAssert.ThrowingCallable result = () -> this.tokenService.validateAndGetActiveToken(testToken.getToken());

        //then
        assertThatThrownBy(result).hasMessage(ExceptionCode.TOKEN_NOT_ACTIVATED.getMessage());
    }

    @Test
    @DisplayName("만료 상태 토큰 검증 실패")
    void testValidateActiveWithExpiredToken() {
        //given
        WaitingQueueToken testToken = new WaitingQueueToken(0);
        testToken.expire();
        when(mockWaitingQueueTokenRepository.getTokenByTokenString(testToken.getToken())).thenReturn(testToken);

        //when
        ThrowableAssert.ThrowingCallable result = () -> this.tokenService.validateAndGetActiveToken(testToken.getToken());

        //then
        assertThatThrownBy(result).hasMessage(ExceptionCode.TOKEN_EXPIRED.getMessage());
    }
}