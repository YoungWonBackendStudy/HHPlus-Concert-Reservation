package io.hhplus.concert.domain.concert;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ConcertServiceUnitTest {
    private final ConcertService concertService;
    private final ConcertRepository mockConcertRepository;

    public ConcertServiceUnitTest() {
        this.mockConcertRepository = mock(ConcertRepository.class);
        this.concertService = new ConcertService(mockConcertRepository);
    }
    
    @Test
    @DisplayName("콘서트 조회 성공 테스트")
    public void testGetConcerts() {
        //given
        var expectedConcerts = List.of(new Concert(0L, "아이유 콘서트", "아이유 콘서트 2024.07.12"));
        when(mockConcertRepository.getConcerts(anyInt(), anyInt())).thenReturn(expectedConcerts);
        
        //when
        var resConcerts = this.concertService.getConcerts(0);

        //then
        assertThat(resConcerts).isNotNull();
        assertThat(resConcerts).isEqualTo(expectedConcerts);
    }

    @Test
    @DisplayName("콘서트 스케줄 조회 성공 테스트")
    public void testGetConcertSchedules() {
        //given
        var concertId = 0L;
        var expectedConcertSchedules = List.of(new ConcertSchedule(0L, concertId, "잠실", new Date(), new Date(), new Date()));
        when(mockConcertRepository.getConcertSchedulesByConcertId(concertId)).thenReturn(expectedConcertSchedules);
        
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
        var concertScheduleId = 0L;
        var expectedConcertSeats = List.of(new ConcertSeat(0L, concertScheduleId, "R1", 0L, false));
        var concertSchedule = new ConcertSchedule(0L, 0L, "잠실", new Date(), new Date(System.currentTimeMillis() + 24* 60*1000L), new Date());
        when(mockConcertRepository.getConcertScheduleById(concertScheduleId)).thenReturn(concertSchedule);
        when(mockConcertRepository.getConcertSeatsByConcertScheduleId(concertScheduleId)).thenReturn(expectedConcertSeats);
        
        //when
        var resConcertSeats = this.concertService.getConcertSeats(concertScheduleId);

        //then
        assertThat(resConcertSeats).isNotNull();
        assertThat(resConcertSeats).isEqualTo(expectedConcertSeats);
    }

    @Test
    @DisplayName("좌석을 예약하면 좌석의 reserved 값이 true로 변경")
    public void testReserveConcertSeats() {
        //given
        var concertSeats = List.of(new ConcertSeat(0L, 0L, "R1", 0L, false));

        //when
        concertService.reserveConcertSeats(concertSeats);

        //then
        verify(mockConcertRepository).saveConcertSeats(argThat(seats -> {
            assertThat(seats).isNotNull();
            seats.forEach(seat -> assertThat(seat.getReserved()).isTrue());
            return true;
        }));
    }

    @Test
    @DisplayName("좌석 예약을 만료시키면 좌석 reserved 값이 false로 변경")
    public void testExpireConcertSeats() {
        //given
        var seatIds = List.of(0L);
        when(mockConcertRepository.getConcertSeatsByIdIn(seatIds))
                .thenReturn(List.of(new ConcertSeat(0L, 0L, "R1", 0L, true)));

        //when
        concertService.expireConcertSeats(seatIds);

        //then
        verify(mockConcertRepository).saveConcertSeats(argThat(seats -> {
            assertThat(seats).isNotNull();
            seats.forEach(seat -> assertThat(seat.getReserved()).isFalse());
            return true;
        }));
    }
}
