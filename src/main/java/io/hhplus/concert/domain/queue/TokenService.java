package io.hhplus.concert.domain.queue;

import java.util.Date;
import java.util.List;

import io.hhplus.concert.support.exception.CustomNotFoundException;
import org.springframework.stereotype.Service;

import io.hhplus.concert.support.exception.ExceptionCode;

@Service
public class TokenService {
    private final long expireDurationInMilli = 30 * 60 * 1000L;

    QueueTokenRepository queueTokenRepository;

    public TokenService(QueueTokenRepository queueTokenRepository) {
        this.queueTokenRepository = queueTokenRepository;
    }

    public QueueToken validateAndGetWaitingToken(long userId) {
        QueueToken queueToken;
        try {
            queueToken = queueTokenRepository.getActiveTokenByUserId(userId);
        } catch(CustomNotFoundException e) {
            if(!e.getCode().equals(ExceptionCode.TOKEN_NOT_FOUND)) throw e;

            queueToken = new QueueToken(userId);
            queueToken = this.queueTokenRepository.saveToken(queueToken);
        }

        queueToken.validateWaiting();
        return queueToken;
    }

    public QueueToken validateAndGetActiveToken(String token) {
        var queueToken = this.queueTokenRepository.getTokenByTokenString(token);
    
        queueToken.validateActivation();
        return queueToken;
    }

    public void expireTokens() {
        Date tokenExpireStandard = new Date(System.currentTimeMillis() - expireDurationInMilli);
        List<QueueToken> tokensToExpire = queueTokenRepository.getActiveTokensActivatedAtBefore(tokenExpireStandard);

        if(tokensToExpire == null || tokensToExpire.isEmpty()) return;

        tokensToExpire.forEach(QueueToken::expire);
        queueTokenRepository.saveAllTokens(tokensToExpire);
    }

    public void expireToken(String token) {
        QueueToken queueToken = queueTokenRepository.getTokenByTokenString(token);
        queueToken.expire();
        queueTokenRepository.saveToken(queueToken);
    }
}
