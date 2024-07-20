package io.hhplus.concert.domain.queue;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import io.hhplus.concert.support.exception.CustomBadRequestException;
import io.hhplus.concert.support.exception.ExceptionCode;

@Service
public class TokenService {
    private final long expireDurationInMilli = 30 * 60 * 1000l;

    QueueTokenRepository queueTokenRepository;

    public TokenService(QueueTokenRepository queueTokenRepository) {
        this.queueTokenRepository = queueTokenRepository;
    }

    public QueueToken getToken(long userId) {
        var queueToken = queueTokenRepository.getActiveTokenByUserId(userId);
        if(queueToken != null) {
            queueToken.validateWaiting();
            return queueToken;
        }

        queueToken = new QueueToken(userId);
        queueToken = this.queueTokenRepository.saveToken(queueToken);

        return queueToken;
    }

    public QueueToken validateAndGetActiveToken(String token) {
        var queueToken = this.queueTokenRepository.getTokenByTokenString(token);
        if(queueToken == null) throw new CustomBadRequestException(ExceptionCode.TOKEN_NOT_FOUND);
    
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
