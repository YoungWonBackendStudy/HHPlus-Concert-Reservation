package io.hhplus.concert.domain.waiting;

public interface WaitingRepository {
    public WaitingToken saveToken(WaitingToken waitingToken);
}
