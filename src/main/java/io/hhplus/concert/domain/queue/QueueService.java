package io.hhplus.concert.domain.queue;

import org.springframework.stereotype.Service;

import io.hhplus.concert.domain.queue.QueueToken.TokenStatus;
import jakarta.transaction.Transactional;

@Service
public class QueueService {
    final int queueSize = 50;

    QueueTokenRepository queueTokenRepository;

    public QueueService(QueueTokenRepository queueTokenRepository) {
        this.queueTokenRepository = queueTokenRepository;
    }

    public Long getWaitingTokensAhead(QueueToken token) {
        var lastActiveToken = this.queueTokenRepository.getFirstTokenOrderByActivatedAtDesc(TokenStatus.ACTIVE);
        if(lastActiveToken == null) return token.getId();
    
        return token.getId() - lastActiveToken.getId();
    }

    @Transactional
    public void activateTokens() {
        var activeTokens = queueTokenRepository.getTokensByStatus(TokenStatus.ACTIVE, queueSize + 1);
        if (activeTokens.size() >= queueSize)
            return;

        var tokensToActivate = queueTokenRepository.getTokensByStatus(TokenStatus.WAITING,
                queueSize - activeTokens.size());
        if (tokensToActivate == null || tokensToActivate.isEmpty())
            return;

        tokensToActivate.stream().forEach(QueueToken::activate);
        queueTokenRepository.saveAllTokens(tokensToActivate);
    }
}
