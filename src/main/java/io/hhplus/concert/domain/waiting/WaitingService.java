package io.hhplus.concert.domain.waiting;

import org.springframework.stereotype.Service;

@Service
public class WaitingService {
    WaitingRepository waitingRepository;

    public WaitingService(WaitingRepository waitingRepository) {
        this.waitingRepository = waitingRepository;
    }

    public String getToken(long userId) {
        var waitingToken = new WaitingToken(userId);
        return waitingToken.getToken();
    }
}
