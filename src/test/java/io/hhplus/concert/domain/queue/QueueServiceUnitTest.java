package io.hhplus.concert.domain.queue;

import io.hhplus.concert.domain.queue.WaitingQueueToken.TokenStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class QueueServiceUnitTest {
    public final QueueService queueService;
    public final WaitingQueueTokenRepository mockWaitingQueueTokenRepository;

    public QueueServiceUnitTest() {
        this.mockWaitingQueueTokenRepository = mock(WaitingQueueTokenRepository.class);
        this.queueService = new QueueService(mockWaitingQueueTokenRepository);
    }

    @Test
    @DisplayName("대기 인원 조회 테스트")
    void testGetWaitingTokensAhead() {
        //given
        long myWaitingId = 30;
        long firstWaitingTokenId = 10;
        WaitingQueueToken token = new WaitingQueueToken(myWaitingId, "myToken", TokenStatus.WAITING, 0, null, null, null);
        WaitingQueueToken firstWaitingToken = new WaitingQueueToken(firstWaitingTokenId, "firstWaitingToken", TokenStatus.WAITING, 1, null, null, null);
        when(this.mockWaitingQueueTokenRepository.getFirstTokenByStatus(TokenStatus.WAITING)).thenReturn(firstWaitingToken);

        //when
        long waitingTokensAhead = queueService.getWaitingTokensAhead(token);

        //then
        assertThat(waitingTokensAhead).isEqualTo(myWaitingId - firstWaitingTokenId);
    }

    @Test
    @DisplayName("대기 중인 토큰 활성화 테스트")
    void testActivateTokens() {
        //given
        var tokensToActivate = List.of(new WaitingQueueToken(0), new WaitingQueueToken(1), new WaitingQueueToken(2));
        when(mockWaitingQueueTokenRepository.getTokensByStatus(eq(TokenStatus.ACTIVE), anyInt())).thenReturn(List.of());
        when(mockWaitingQueueTokenRepository.getTokensByStatus(eq(TokenStatus.WAITING), anyInt())).thenReturn(tokensToActivate);

        //when
        queueService.activateTokens();

        //then
        verify(mockWaitingQueueTokenRepository).saveTokens(anyList());
    }

    @Test
    @DisplayName("활성 인원이 이미 50명인 경우 활성하지 않고 종료")
    void testOverActivateTokens() {
        //given
        var activeTokens = new ArrayList<WaitingQueueToken>(50);
        for(int i = 0; i < 50; i++) {
            var activeToken = new WaitingQueueToken(i);
            activeToken.activate();
            activeTokens.add(activeToken);
        }
        var tokensToActivate = List.of(new WaitingQueueToken(0), new WaitingQueueToken(1), new WaitingQueueToken(2));
        when(mockWaitingQueueTokenRepository.getTokensByStatus(eq(TokenStatus.ACTIVE), anyInt())).thenReturn(activeTokens);
        when(mockWaitingQueueTokenRepository.getTokensByStatus(eq(TokenStatus.WAITING), anyInt())).thenReturn(tokensToActivate);

        //when
        queueService.activateTokens();

        //then
        verify(mockWaitingQueueTokenRepository, times(0)).saveTokens(anyList());
    }
}
