package io.hhplus.concert.infra.waiting;

import org.springframework.stereotype.Repository;

import io.hhplus.concert.domain.waiting.WaitingRepository;
import io.hhplus.concert.domain.waiting.WaitingToken;

@Repository
public class WaitingRepositoryImpl implements WaitingRepository{
    WaitingTokenJpaRepository waitingTokenJpaRepository;

    public WaitingRepositoryImpl (WaitingTokenJpaRepository waitingTokenJpaRepository) {
        this.waitingTokenJpaRepository = waitingTokenJpaRepository;
    }

    @Override
    public WaitingToken saveToken(WaitingToken waitingToken) {
        var entity = new WaitingTokenEntity(waitingToken);
        return waitingTokenJpaRepository.save(entity).toDomain();
    }
}
