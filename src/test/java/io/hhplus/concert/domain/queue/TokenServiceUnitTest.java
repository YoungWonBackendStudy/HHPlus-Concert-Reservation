package io.hhplus.concert.domain.queue;

import io.hhplus.concert.support.exception.CustomBadRequestException;
import io.hhplus.concert.support.exception.CustomNotFoundException;
import io.hhplus.concert.support.exception.ExceptionCode;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TokenServiceUnitTest {
    TokenService tokenService;
    WaitingQueueTokenRepository mockWaitingQueueTokenRepository;
    ActiveTokenRepository mockActiveTokenRepository;
    
    public TokenServiceUnitTest() {
        this.mockWaitingQueueTokenRepository = mock(WaitingQueueTokenRepository.class);
        this.mockActiveTokenRepository = mock(ActiveTokenRepository.class);
        this.tokenService = new TokenService(this.mockWaitingQueueTokenRepository, mockActiveTokenRepository);
    }

    @Test
    @DisplayName("토큰 발급 성공 테스트")
    void testGetQueueToken() {
        //given
        when(mockWaitingQueueTokenRepository.getWaitingQueueTokenByTokenStr(anyString()))
                .thenThrow(new CustomNotFoundException(ExceptionCode.WAITING_TOKEN_NOT_FOUND));
        when(mockWaitingQueueTokenRepository.saveWaitingQueueToken(any(WaitingQueueToken.class))).thenAnswer(returnsFirstArg());

        //when
        var expectedIssueTime = System.currentTimeMillis();
        var token = this.tokenService.getWaitingQueueToken(null);

        //then
        assertThat(token).isNotNull();
        assertThat(token.getIssuedAtInMillis()).isCloseTo(expectedIssueTime, within(1000L));
    }
    
    @Test
    @DisplayName("토큰 ACTIVE 검증 성공 테스트")
    void testValidateActiveToken() {
        //given
        WaitingQueueToken testToken = new WaitingQueueToken();
        var activeToken = new ActiveToken(testToken);
        when(mockActiveTokenRepository.getActiveTokenByTokenString(testToken.getToken())).thenReturn(activeToken);

        //when
        ThrowableAssert.ThrowingCallable result = () -> this.tokenService.getActiveToken(testToken.getToken());

        //then
        assertThatCode(result).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("활성 토큰으로 대기열 조회시 활성화된 토큰입니다 오류 발생")
    void testActivatedToken() {
        //given
        WaitingQueueToken testToken = new WaitingQueueToken();
        var activeToken = new ActiveToken(testToken);
        when(mockActiveTokenRepository.getActiveTokenByTokenString(testToken.getToken())).thenReturn(activeToken);

        //when
        ThrowableAssert.ThrowingCallable result = () -> this.tokenService.getWaitingQueueToken(testToken.getToken());

        //then
        assertThatThrownBy(result).hasMessage(new CustomBadRequestException(ExceptionCode.TOKEN_IS_ACTIVATED).getMessage());
    }

    @Test
    @DisplayName("토큰 만료 테스트")
    void testExpireToken(){
        //given
        WaitingQueueToken token = new WaitingQueueToken();
        ActiveToken activeToken = new ActiveToken(token);
        when(mockActiveTokenRepository.getActiveTokenByTokenString(token.getToken())).thenReturn(activeToken);

        //when
        tokenService.expireToken(token.getToken());

        //then
        verify(mockActiveTokenRepository).deleteActiveToken(eq(activeToken));
    }

}