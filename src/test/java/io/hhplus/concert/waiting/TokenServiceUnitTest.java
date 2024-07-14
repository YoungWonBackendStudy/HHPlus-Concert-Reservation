package io.hhplus.concert.waiting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.List;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.hhplus.concert.domain.waiting.TokenService;
import io.hhplus.concert.domain.waiting.WaitingToken;
import io.hhplus.concert.domain.waiting.WaitingToken.TokenStatus;
import io.hhplus.concert.domain.waiting.WaitingTokenRepository;

public class TokenServiceUnitTest {
    TokenService tokenService;
    WaitingTokenRepository mockWaitingTokenRepository;
    
    public TokenServiceUnitTest() {
        this.mockWaitingTokenRepository = mock(WaitingTokenRepository.class);
        this.tokenService = new TokenService(this.mockWaitingTokenRepository);
    }

    @Test
    @DisplayName("토큰 발급 성공 테스트")
    void testGetWaitingToken() {
        //given
        long userId = 0;
        when(mockWaitingTokenRepository.saveToken(any(WaitingToken.class))).thenAnswer(returnsFirstArg());

        //when
        var waitingToken = this.tokenService.getToken(userId);

        //then
        assertThat(waitingToken).isNotNull();
        assertThat(waitingToken.getUserId()).isEqualTo(userId);
        assertThat(waitingToken.getStatus()).isEqualTo(TokenStatus.WAITING);
    }
    
    @Test
    @DisplayName("토큰 ACTIVE 검증 성공 테스트")
    void testValidateActiveToken() {
        //given
        WaitingToken testToken = new WaitingToken(0);
        testToken.activate();
        when(mockWaitingTokenRepository.getTokenByTokenString(testToken.getToken())).thenReturn(testToken);

        //when
        ThrowableAssert.ThrowingCallable result = () -> {
            this.tokenService.validateAndGetActiveToken(testToken.getToken());
        };

        //then
        assertThatCode(result).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("토큰 만료 테스트")
    void testExpireTokens(){
        //given
        List<WaitingToken> tokensToExpire = List.of(new WaitingToken(0));
        when(mockWaitingTokenRepository.getActiveTokensActivatedAtBefore(any())).thenReturn(tokensToExpire);

        //when
        tokenService.expireTokens();

        //then
        verify(mockWaitingTokenRepository).saveAllTokens(anyList());
    }

    @Test
    @DisplayName("WAITING 상태 토큰 검증 실패")
    void testValidateActiveWithWaitingToken() {
        //given
        WaitingToken testToken = new WaitingToken(0);
        when(mockWaitingTokenRepository.getTokenByTokenString(testToken.getToken())).thenReturn(testToken);

        //when
        ThrowableAssert.ThrowingCallable result = () -> {
            this.tokenService.validateAndGetActiveToken(testToken.getToken());
        };

        //then
        assertThatThrownBy(result).hasMessage("토큰이 활성화 상태가 아닙니다.");
    }

    @Test
    @DisplayName("만료 상태 토큰 검증 실패")
    void testValidateActiveWithExpiredToken() {
        //given
        WaitingToken testToken = new WaitingToken(0);
        testToken.expire();
        when(mockWaitingTokenRepository.getTokenByTokenString(testToken.getToken())).thenReturn(testToken);

        //when
        ThrowableAssert.ThrowingCallable result = () -> {
            this.tokenService.validateAndGetActiveToken(testToken.getToken());
        };

        //then
        assertThatThrownBy(result).hasMessage("토큰이 활성화 상태가 아닙니다.");
    }
}