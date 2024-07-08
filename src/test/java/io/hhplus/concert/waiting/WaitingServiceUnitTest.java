package io.hhplus.concert.waiting;

import static org.mockito.Mockito.mock;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.hhplus.concert.domain.waiting.WaitingRepository;
import io.hhplus.concert.domain.waiting.WaitingService;

public class WaitingServiceUnitTest {
    WaitingService waitingService;
    WaitingRepository mockWaitingRepository;
    
    public WaitingServiceUnitTest() {
        this.mockWaitingRepository = mock(WaitingRepository.class);
        this.waitingService = new WaitingService(this.mockWaitingRepository);
    }

    @Test
    @DisplayName("Waiting Token 발급 성공 테스트")
    void testGetWaitingToken() {
        //given
        long userId = 0;

        //when
        String waitingToken = this.waitingService.getToken(userId);

        //then
        assertThat(waitingToken).isNotBlank();
    }   
}