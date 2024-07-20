package io.hhplus.concert.waiting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
        when(this.mockWaitingTokenRepository.getFirstTokenOrderByActivatedAtDesc(TokenStatus.ACTIVE)).thenReturn(lastActiveToken);

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
        when(mockWaitingTokenRepository.getTokensByStatus(eq(TokenStatus.ACTIVE), anyInt())).thenReturn(List.of());
        when(mockWaitingTokenRepository.getTokensByStatus(eq(TokenStatus.WAITING), anyInt())).thenReturn(tokensToActivate);

        //when
        waitingService.activateWaitings();

        //then
        verify(mockWaitingTokenRepository).saveAllTokens(anyList());
    }

    @Test
    @DisplayName("활성 인원이 이미 50명인 경우 활성하지 않고 종료")
    void testOverActivateWaitings() {
        //given
        var activeTokens = new ArrayList<WaitingToken>(50);
        for(int i = 0; i < 50; i++) {
            var activeToken = new WaitingToken(i);
            activeToken.activate();
            activeTokens.add(activeToken);
        }
        var tokensToActivate = List.of(new WaitingToken(0), new WaitingToken(1), new WaitingToken(2));
        when(mockWaitingTokenRepository.getTokensByStatus(eq(TokenStatus.ACTIVE), anyInt())).thenReturn(activeTokens);
        when(mockWaitingTokenRepository.getTokensByStatus(eq(TokenStatus.WAITING), anyInt())).thenReturn(tokensToActivate);

        //when
        waitingService.activateWaitings();

        //then
        verify(mockWaitingTokenRepository, times(0)).saveAllTokens(anyList());
    }
}
