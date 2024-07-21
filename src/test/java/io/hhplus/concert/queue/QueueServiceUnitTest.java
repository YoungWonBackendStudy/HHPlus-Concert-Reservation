package io.hhplus.concert.queue;

import io.hhplus.concert.domain.queue.QueueService;
import io.hhplus.concert.domain.queue.QueueToken;
import io.hhplus.concert.domain.queue.QueueToken.TokenStatus;
import io.hhplus.concert.domain.queue.QueueTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class QueueServiceUnitTest {
    public final QueueService queueService;
    public final QueueTokenRepository mockQueueTokenRepository;

    public QueueServiceUnitTest() {
        this.mockQueueTokenRepository = mock(QueueTokenRepository.class);
        this.queueService = new QueueService(mockQueueTokenRepository);
    }

    @Test
    @DisplayName("대기 인원 조회 테스트")
    void testGetWaitingTokensAhead() {
        //given
        long myWaitingId = 30;
        long firstWaitingTokenId = 10;
        QueueToken token = new QueueToken(myWaitingId, "myToken", TokenStatus.WAITING, 0, null, null, null);
        QueueToken firstWaitingToken = new QueueToken(firstWaitingTokenId, "firstWaitingToken", TokenStatus.WAITING, 1, null, null, null);
        when(this.mockQueueTokenRepository.getFirstTokenByStatus(TokenStatus.WAITING)).thenReturn(firstWaitingToken);

        //when
        long waitingTokensAhead = queueService.getWaitingTokensAhead(token);

        //then
        assertThat(waitingTokensAhead).isEqualTo(myWaitingId - firstWaitingTokenId);
    }

    @Test
    @DisplayName("대기 중인 토큰 활성화 테스트")
    void testActivateTokens() {
        //given
        var tokensToActivate = List.of(new QueueToken(0), new QueueToken(1), new QueueToken(2));
        when(mockQueueTokenRepository.getTokensByStatus(eq(TokenStatus.ACTIVE), anyInt())).thenReturn(List.of());
        when(mockQueueTokenRepository.getTokensByStatus(eq(TokenStatus.WAITING), anyInt())).thenReturn(tokensToActivate);

        //when
        queueService.activateTokens();

        //then
        verify(mockQueueTokenRepository).saveAllTokens(anyList());
    }

    @Test
    @DisplayName("활성 인원이 이미 50명인 경우 활성하지 않고 종료")
    void testOverActivateTokens() {
        //given
        var activeTokens = new ArrayList<QueueToken>(50);
        for(int i = 0; i < 50; i++) {
            var activeToken = new QueueToken(i);
            activeToken.activate();
            activeTokens.add(activeToken);
        }
        var tokensToActivate = List.of(new QueueToken(0), new QueueToken(1), new QueueToken(2));
        when(mockQueueTokenRepository.getTokensByStatus(eq(TokenStatus.ACTIVE), anyInt())).thenReturn(activeTokens);
        when(mockQueueTokenRepository.getTokensByStatus(eq(TokenStatus.WAITING), anyInt())).thenReturn(tokensToActivate);

        //when
        queueService.activateTokens();

        //then
        verify(mockQueueTokenRepository, times(0)).saveAllTokens(anyList());
    }
}
