package io.hhplus.concert.infra.concert;

import java.util.List;

import io.hhplus.concert.domain.concert.*;
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
    public List<ConcertSchedule> getConcertSchedulesByScheduleId(long concertId) {
        return this.concertScheduleJpaRepository.findAll()
            .stream().map(ConcertScheduleEntity::toDomain)
            .toList();
    }

    @Override
    public List<ConcertSeat> getConcertSeatsByConcertScheduleId(long concertScheduleId) {
        return this.concertSeatJpaRepository.findByConcertScheduleId(concertScheduleId)
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
