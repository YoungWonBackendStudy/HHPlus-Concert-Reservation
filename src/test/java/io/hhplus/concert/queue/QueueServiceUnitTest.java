package io.hhplus.concert.queue;

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

import io.hhplus.concert.domain.queue.QueueService;
import io.hhplus.concert.domain.queue.QueueToken;
import io.hhplus.concert.domain.queue.QueueToken.TokenStatus;
import io.hhplus.concert.domain.queue.QueueTokenRepository;

public class QueueServiceUnitTest {
    public QueueService queueService;
    public QueueTokenRepository mockQueueTokenRepository;

    public QueueServiceUnitTest() {
        this.mockQueueTokenRepository = mock(QueueTokenRepository.class);
        this.queueService = new QueueService(mockQueueTokenRepository);
    }

    @Test
    @DisplayName("대기 인원 조회 테스트")
    void testGetWaitingTokensAhead() {
        //given
        long myWaitingId = 30;
        long lastActiveWaitingId = 10;
        QueueToken token = new QueueToken(myWaitingId, "lastActiveToken", TokenStatus.WAITING, 0, null, null, null);
        QueueToken lastActiveToken = new QueueToken(lastActiveWaitingId, "lastActiveToken", TokenStatus.ACTIVE, 1, null, null, null);
        when(this.mockQueueTokenRepository.getFirstTokenOrderByActivatedAtDesc(TokenStatus.ACTIVE)).thenReturn(lastActiveToken);

        //when
        long waitingTokensAhead = queueService.getWaitingTokensAhead(token);

        //then
        assertThat(waitingTokensAhead).isEqualTo(myWaitingId - lastActiveWaitingId);
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
