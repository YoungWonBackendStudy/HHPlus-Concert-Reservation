package io.hhplus.concert.infra.concert;

import java.util.List;

import io.hhplus.concert.domain.concert.*;
import io.hhplus.concert.support.exception.CustomNotFoundException;
import io.hhplus.concert.support.exception.ExceptionCode;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

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
    public List<Concert> getConcerts() {
        return this.concertJpaRepository.findAll()
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
    public List<ConcertSeat> getConcertSeatsByConcertScheduleId(long concertScheduleId) {
        var concertScheduleEntity = this.concertScheduleJpaRepository.findById(concertScheduleId);
        if(concertScheduleEntity.isEmpty()) throw new CustomNotFoundException(ExceptionCode.CONCERT_SCHEDULE_NOT_FOUND);

        return this.concertSeatJpaRepository.findByConcertScheduleId(concertScheduleEntity.get().getId())
            .stream().map(ConcertSeatEntity::toDomain)
            .toList();
    }

    @Override
    @Transactional
    public List<ConcertSeat> getAndLockConcertSeatsByIdIn(List<Long> concertSeatIds) {
        return this.concertSeatJpaRepository.findAndLockByIdIn(concertSeatIds)
            .stream().map(ConcertSeatEntity::toDomain)
            .toList();
    }
}
