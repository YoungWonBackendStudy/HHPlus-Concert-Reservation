package io.hhplus.concert.infra.concert;

import io.hhplus.concert.domain.concert.Concert;
import io.hhplus.concert.domain.concert.ConcertRepository;
import io.hhplus.concert.domain.concert.ConcertSchedule;
import io.hhplus.concert.domain.concert.ConcertSeat;
import io.hhplus.concert.support.exception.CustomNotFoundException;
import io.hhplus.concert.support.exception.ExceptionCode;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConcertRepositoryImpl implements ConcertRepository{
    private final ConcertJpaRepository concertJpaRepository;
    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;
    private final ConcertSeatJpaRepository concertSeatJpaRepository;

    public ConcertRepositoryImpl(ConcertJpaRepository concertJpaRepository, ConcertScheduleJpaRepository concertScheduleJpaRepository, ConcertSeatJpaRepository concertSeatJpaRepository) {
        this.concertJpaRepository = concertJpaRepository;
        this.concertScheduleJpaRepository = concertScheduleJpaRepository;
        this.concertSeatJpaRepository = concertSeatJpaRepository;
    }

    @Override
    public List<Concert> getConcerts(int page, int pageSize) {
        return this.concertJpaRepository.findConcertsAvailableToday(PageRequest.of(page, pageSize))
            .stream().map(ConcertEntity::toDomain)
            .toList();
    }

    @Override
    public List<ConcertSchedule> getConcertSchedulesByConcertId(long concertId) {
        var concertEntity = this.concertJpaRepository.findById(concertId);
        if(concertEntity.isEmpty()) throw new CustomNotFoundException(ExceptionCode.CONCERT_NOT_FOUND);

        return this.concertScheduleJpaRepository.findByConcertId(concertEntity.get().getId())
            .stream().map(ConcertScheduleEntity::toDomain)
            .toList();
    }

    @Override
    public ConcertSchedule getConcertScheduleById(long concertScheduleId) {
        var concertScheduleEntity = this.concertScheduleJpaRepository.findById(concertScheduleId);
        if(concertScheduleEntity.isEmpty()) throw new CustomNotFoundException(ExceptionCode.CONCERT_SCHEDULE_NOT_FOUND);

        return concertScheduleEntity.get().toDomain();
    }

    @Override
    public List<ConcertSeat> getConcertSeatsByConcertScheduleId(long concertScheduleId) {
        return this.concertSeatJpaRepository.findByConcertScheduleId(concertScheduleId)
            .stream().map(ConcertSeatEntity::toDomain)
            .toList();
    }

    @Override
    public List<ConcertSeat> getConcertSeatsByIdIn(List<Long> concertSeatIds) {
        return this.concertSeatJpaRepository.findByIdIn(concertSeatIds)
                .stream().map(ConcertSeatEntity::toDomain)
                .toList();
    }

    @Override
    public List<ConcertSeat> saveConcertSeats(List<ConcertSeat> concertSeats) {
        var entities = concertSeats.stream().map(ConcertSeatEntity::new).toList();
        return this.concertSeatJpaRepository.saveAll(entities)
                .stream().map(ConcertSeatEntity::toDomain)
                .toList();
    }
}
