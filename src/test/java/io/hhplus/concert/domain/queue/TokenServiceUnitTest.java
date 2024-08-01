package io.hhplus.concert.domain.queue;

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
        long userId = 0;
        when(mockWaitingQueueTokenRepository.getWaitingQueueTokenByUserId(anyLong()))
                .thenThrow(new CustomNotFoundException(ExceptionCode.WAITING_TOKEN_NOT_FOUND));
        when(mockWaitingQueueTokenRepository.saveWaitingQueueToken(any(WaitingQueueToken.class))).thenAnswer(returnsFirstArg());

        //when
        var expectedIssueTime = System.currentTimeMillis();
        var token = this.tokenService.getWaitingQueueToken(userId);

        //then
        assertThat(token).isNotNull();
        assertThat(token.getUserId()).isEqualTo(userId);
        assertThat(token.getIssuedAtInMillis()).isCloseTo(expectedIssueTime, within(1000L));
    }
    
    @Test
    @DisplayName("토큰 ACTIVE 검증 성공 테스트")
    void testValidateActiveToken() {
        //given
        WaitingQueueToken testToken = new WaitingQueueToken(0);
        var activeToken = new ActiveToken(testToken);
        when(mockActiveTokenRepository.getActiveTokenByTokenString(testToken.getToken())).thenReturn(activeToken);

        //when
        ThrowableAssert.ThrowingCallable result = () -> this.tokenService.getActiveToken(testToken.getToken());

        //then
        assertThatCode(result).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("토큰 만료 테스트")
    void testExpireToken(){
        //given
        WaitingQueueToken token = new WaitingQueueToken(0);
        ActiveToken activeToken = new ActiveToken(token);
        when(mockActiveTokenRepository.getActiveTokenByTokenString(token.getToken())).thenReturn(activeToken);

        //when
        tokenService.expireToken(token.getToken());

        //then
        verify(mockActiveTokenRepository).deleteActiveToken(eq(activeToken));
    }

}