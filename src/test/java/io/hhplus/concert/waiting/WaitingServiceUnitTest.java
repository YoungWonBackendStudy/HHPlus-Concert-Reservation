package io.hhplus.concert.waiting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.hhplus.concert.domain.waiting.WaitingService;
import io.hhplus.concert.domain.waiting.WaitingToken;
import io.hhplus.concert.domain.waiting.WaitingToken.TokenStatus;
import io.hhplus.concert.domain.waiting.WaitingTokenRepository;

public class WaitingServiceUnitTest {
    public WaitingService waitingService;
    public WaitingTokenRepository mockWaitingTokenRepository;

    public WaitingServiceUnitTest() {
        this.mockWaitingTokenRepository = mock(WaitingTokenRepository.class);
        this.waitingService = new WaitingService(mockWaitingTokenRepository);
    }

    @Test
    @DisplayName("대기 인원 조회 테스트")
    void testGetWaitingsAhead() {
        //given
        long myWaitingId = 30;
        long lastActiveWaitingId = 10;
        WaitingToken token = new WaitingToken(myWaitingId, "lastActiveToken", TokenStatus.WAITING, 0, null, null, null);
        WaitingToken lastActiveToken = new WaitingToken(lastActiveWaitingId, "lastActiveToken", TokenStatus.ACTIVE, 1, null, null, null);
        when(this.mockWaitingTokenRepository.getLastTokenByStatus(TokenStatus.ACTIVE)).thenReturn(lastActiveToken);

        //when
        long waitingsAhead = waitingService.getWaitingsAhead(token);

        //then
        assertThat(waitingsAhead).isEqualTo(myWaitingId - lastActiveWaitingId);
    }

    @Test
    @DisplayName("대기 중인 토큰 활성화 테스트")
    void testActivateWaitings() {
        //given
        var tokensToActivate = List.of(new WaitingToken(0), new WaitingToken(1), new WaitingToken(2));
        when(mockWaitingTokenRepository.getTokensByStatus(TokenStatus.ACTIVE)).thenReturn(List.of());
        when(mockWaitingTokenRepository.getTokensByStatusAndSize(TokenStatus.WAITING, anyInt())).thenReturn(tokensToActivate);

        //when
        waitingService.activateWaitings();

        //then
        verify(mockWaitingTokenRepository).saveAllTokens(anyList());
    }
}
