package io.hhplus.concert.application.concert;

import groovy.util.logging.Slf4j;
import io.hhplus.concert.infra.concert.ConcertJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("query-test")
@Slf4j
public class ConcertQueryIntegTest {
    private static final Logger log = LoggerFactory.getLogger(ConcertQueryIntegTest.class);
    @Autowired
    ConcertFacade concertFacade;

    @Autowired
    ConcertJpaRepository concertJpaRepository;

    @Test
    @DisplayName("콘서트 Paging 조회 캐싱 성능 테스트")
    void testConcertCachingTest() {
        //given
        int testCnt = 100;
        int page = 1;

        List<Long> executionTimes = new ArrayList<>(testCnt);
        //when
        for(int i = 0; i < testCnt; i++) {
            long start = System.currentTimeMillis();
            concertFacade.getConcerts(page);
            executionTimes.add(System.currentTimeMillis() - start);
        }

        //then
        var totalExecutionTimes = executionTimes.stream().reduce(0L, Long::sum);
        assertThat(executionTimes).isNotEmpty();
        assertThat(totalExecutionTimes).isLessThan(500_000L);
    }

    @Test
    @DisplayName("예약 가능 콘서트 조회 쿼리 성능 테스트")
    void testGetReservedOver5minsAndNotCompleted() {
        var timeBefore = System.currentTimeMillis();
        concertJpaRepository.findConcertsAvailableToday(Pageable.unpaged());
        log.info("예약 가능 콘서트 조회: {} ms", System.currentTimeMillis() - timeBefore);
    }
}
