package io.hhplus.concert.domain.queue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class QueueServiceUnitTest {
    private final QueueService queueService;
    private final WaitingQueueTokenRepository mockWaitingQueueTokenRepository;
    private final ActiveTokenRepository mockActiveTokenRepository;

    public QueueServiceUnitTest() {
        this.mockWaitingQueueTokenRepository = mock(WaitingQueueTokenRepository.class);
        this.mockActiveTokenRepository = mock(ActiveTokenRepository.class);
        this.queueService = new QueueService(mockWaitingQueueTokenRepository, mockActiveTokenRepository);
    }

    @Test
    @DisplayName("대기 인원 조회 테스트")
    void testGetWaitingTokensAhead() {
        //given
        long myWaitingId = 30;
        WaitingQueueToken token = new WaitingQueueToken("myToken", System.currentTimeMillis());
        when(this.mockWaitingQueueTokenRepository.getWaitingQueueTokenRank(token)).thenReturn(myWaitingId);

        //when
        long waitingTokensAhead = queueService.getWaitingTokensAhead(token);

        //then
        assertThat(waitingTokensAhead).isEqualTo(myWaitingId);
    }

    @Test
    @DisplayName("대기 중인 토큰 활성화 테스트")
    void testActivateTokens() {
        //given
        var tokensToActivate = List.of(new WaitingQueueToken(), new WaitingQueueToken(), new WaitingQueueToken());
        when(mockWaitingQueueTokenRepository.dequeFirstNWaitingQueueTokens(anyLong())).thenReturn(tokensToActivate);

        //when
        queueService.activateTokens();

        //then
        verify(mockActiveTokenRepository).saveActiveTokens(argThat(activeTokens -> {
            assertThat(activeTokens.size()).isEqualTo(tokensToActivate.size());
            return true;
        }));
    }
}
