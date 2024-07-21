package io.hhplus.concert.domain.queue;

import io.hhplus.concert.support.exception.CustomNotFoundException;
import io.hhplus.concert.support.exception.ExceptionCode;
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
        QueueToken firstWaitingToken;
        try{
            firstWaitingToken = this.queueTokenRepository.getFirstTokenByStatus(TokenStatus.WAITING);
        }catch(CustomNotFoundException e) {
            if(!e.getCode().equals(ExceptionCode.TOKEN_NOT_FOUND)) throw e;

            firstWaitingToken = token;
        }
        return token.getId() - firstWaitingToken.getId();
    }

    @Transactional
    public void activateTokens() {
        var activeTokens = queueTokenRepository.getTokensByStatus(TokenStatus.ACTIVE, queueSize + 1);
        if (activeTokens.size() >= queueSize)
            return;

        var tokensToActivate = queueTokenRepository.getTokensByStatus(TokenStatus.WAITING, queueSize - activeTokens.size());
        if (tokensToActivate == null || tokensToActivate.isEmpty())
            return;

        tokensToActivate.forEach(QueueToken::activate);
        queueTokenRepository.saveAllTokens(tokensToActivate);
    }
}
