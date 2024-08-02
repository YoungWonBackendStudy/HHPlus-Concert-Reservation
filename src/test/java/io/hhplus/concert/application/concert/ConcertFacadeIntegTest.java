package io.hhplus.concert.application.concert;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(scripts = "classpath:testinit.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ConcertFacadeIntegTest {
    @Autowired
    ConcertFacade concertFacade;

    @Test
    @DisplayName("콘서트/스케줄/좌석 조회 통합 테스트")
    void testConcertIntegTest() {
        //when
        var concerts = concertFacade.getConcerts(0);
        
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

    @Test
    @DisplayName("콘서트 Paging 조회 캐싱 성능 테스트")
    void testConcertCachingTest() {
        //given
        int testCnt = 100;
        int page = 8;

        List<Long> executionTimes = new ArrayList<Long>(testCnt);
        //when
        for(int i = 0; i < testCnt; i++) {
            long start = System.currentTimeMillis();
            concertFacade.getConcerts(page);
            executionTimes.add(System.currentTimeMillis() - start);
        }

        //then
        assertThat(executionTimes).isNotEmpty();
    }


}
