package io.hhplus.concert.concert;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import io.hhplus.concert.application.concert.ConcertFacade;

@SpringBootTest
@Sql(scripts = "classpath:testinit.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ConcertFacadeIntegTest {
    @Autowired
    ConcertFacade concertFacade;

    @Test
    @DisplayName("콘서트 조회 통합 테스트")
    void testConcertIntegTest() {
        //when
        var concerts = concertFacade.getConcerts();
        
        //then
        assertThat(concerts).isNotNull();
        assertThat(concerts).isNotEmpty();

        //when
        var concertSchedules = concertFacade.getConcertSchedules(concerts.get(0).getId());

        //then
        assertThat(concertSchedules).isNotNull();
        assertThat(concertSchedules).isNotEmpty();

        //when
        var concertSeats = concertFacade.getConcertSeats(concertSchedules.get(0).getId());
        
        //then
        assertThat(concertSeats).isNotNull();
        assertThat(concertSeats).isNotEmpty();
    }
}
