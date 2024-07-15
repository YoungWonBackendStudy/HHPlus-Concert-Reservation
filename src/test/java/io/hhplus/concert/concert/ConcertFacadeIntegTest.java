package io.hhplus.concert.concert;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.hhplus.concert.application.concert.ConcertFacade;
import io.hhplus.concert.domain.waiting.TokenService;
import io.hhplus.concert.domain.waiting.WaitingToken;

@SpringBootTest
public class ConcertFacadeIntegTest {
    ConcertFacade concertFacade;
    TokenService tokenService;

    public ConcertFacadeIntegTest(ConcertFacade concertFacade, TokenService tokenService) {
        this.concertFacade = concertFacade;
        this.tokenService = tokenService;
    }

    @Test
    @DisplayName("콘서트 조회 통합 테스트")
    void testConcertIntegTest() {
        //given
        WaitingToken testToken = tokenService.getToken(0l);

        //when
        var concerts = concertFacade.getConcerts(testToken.getToken());
        
        //then
        assertThat(concerts).isNotNull();
        assertThat(concerts).isNotEmpty();

        //when
        var concertSchedules = concertFacade.getConcertSchedules(testToken.getToken(), concerts.get(0).getId());

        //then
        assertThat(concertSchedules).isNotNull();
        assertThat(concertSchedules).isNotEmpty();

        //when
        var concertSeats = concertFacade.getConcertSeats(testToken.getToken(), concertSchedules.get(0).getId());
        
        //then
        assertThat(concertSeats).isNotNull();
        assertThat(concertSeats).isNotEmpty();
    }
}
