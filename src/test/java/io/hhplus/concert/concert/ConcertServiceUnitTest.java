package io.hhplus.concert.concert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.hhplus.concert.domain.concert.Concert;
import io.hhplus.concert.domain.concert.ConcertRepository;
import io.hhplus.concert.domain.concert.ConcertSchedule;
import io.hhplus.concert.domain.concert.ConcertSeat;
import io.hhplus.concert.domain.concert.ConcertService;

public class ConcertServiceUnitTest {
    ConcertService concertService;
    ConcertRepository mockConcertRepository;

    public ConcertServiceUnitTest() {
        this.mockConcertRepository = mock(ConcertRepository.class);
        this.concertService = new ConcertService(mockConcertRepository);
    }
    
    @Test
    @DisplayName("콘서트 조회 성공 테스트")
    public void testGetConcerts() {
        //given
        var expectedConcerts = List.of(new Concert(0l, "아이유 콘서트", "아이유 콘서트 2024.07.12"));
        when(mockConcertRepository.getConcerts()).thenReturn(expectedConcerts);
        
        //when
        var resConcerts = this.concertService.getConcerts();

        //then
        assertThat(resConcerts).isNotNull();
        assertThat(resConcerts).isEqualTo(expectedConcerts);
    }

    @Test
    @DisplayName("콘서트 스케줄 조회 성공 테스트")
    public void testGetConcertSchedules() {
        //given
        var concertId = 0;
        var expectedConcertSchedules = List.of(new ConcertSchedule(0l, 0l, "잠실", new Date(), new Date(), new Date()));
        when(mockConcertRepository.getConcertSchedulesByScheduleId(concertId)).thenReturn(expectedConcertSchedules);
        
        //when
        var resConcertSchedules = this.concertService.getConcertSchedules(concertId);

        //then
        assertThat(resConcertSchedules).isNotNull();
        assertThat(resConcertSchedules).isEqualTo(expectedConcertSchedules);
    }

    @Test
    @DisplayName("콘서트 좌석 조회 성공 테스트")
    public void testGetConcertSeat() {
        //given
        var concertScheduleId = 0;
        var expectedConcertSeats = List.of(new ConcertSeat(0l, 0l, "R1", 100000l));
        when(mockConcertRepository.getConcertSeatsByConcertScheduleId(concertScheduleId)).thenReturn(expectedConcertSeats);
        
        //when
        var resConcertSeats = this.concertService.getConcertSeats(concertScheduleId);

        //then
        assertThat(resConcertSeats).isNotNull();
        assertThat(resConcertSeats).isEqualTo(expectedConcertSeats);
    }
}
