package io.hhplus.concert.domain.waiting;

import org.springframework.stereotype.Service;

import io.hhplus.concert.domain.waiting.WaitingToken.TokenStatus;
import jakarta.transaction.Transactional;

@Service
public class WaitingService {
    final long waitingSize = 50;

    WaitingTokenRepository waitingTokenRepository;

    public WaitingService(WaitingTokenRepository waitingTokenRepository) {
        this.waitingTokenRepository = waitingTokenRepository;
    }

    public Long getWaitingsAhead(WaitingToken token) {
        var lastActiveToken = this.waitingTokenRepository.getLastTokenByStatus(TokenStatus.ACTIVE);
        return token.getId() - lastActiveToken.getId();
    }

    @Transactional
    public void activateWaitings() {
        var activeTokens = waitingTokenRepository.getTokensByStatus(TokenStatus.ACTIVE);
        if(activeTokens.size() >= waitingSize) return;

        var tokensToActivate = waitingTokenRepository.getTokensByStatusAndSize(TokenStatus.WAITING, waitingSize - activeTokens.size());
        if(tokensToActivate == null || tokensToActivate.isEmpty()) return;
        
        tokensToActivate.stream().forEach(WaitingToken::activate);
        waitingTokenRepository.saveAllTokens(tokensToActivate);
    }
}
