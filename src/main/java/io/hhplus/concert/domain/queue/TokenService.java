package io.hhplus.concert.domain.queue;

import java.util.Date;
import java.util.List;

import io.hhplus.concert.support.exception.CustomNotFoundException;
import org.springframework.stereotype.Service;

import io.hhplus.concert.support.exception.ExceptionCode;

@Service
public class TokenService {
    private final long expireDurationInMilli = 30 * 60 * 1000L;

    WaitingQueueTokenRepository waitingQueueTokenRepository;

    public TokenService(WaitingQueueTokenRepository waitingQueueTokenRepository) {
        this.waitingQueueTokenRepository = waitingQueueTokenRepository;
    }

    public WaitingQueueToken validateAndGetWaitingToken(long userId) {
        WaitingQueueToken waitingQueueToken;
        try {
            waitingQueueToken = waitingQueueTokenRepository.getActiveTokenByUserId(userId);
        } catch(CustomNotFoundException e) {
            if(!e.getCode().equals(ExceptionCode.TOKEN_NOT_FOUND)) throw e;

            waitingQueueToken = new WaitingQueueToken(userId);
            waitingQueueToken = this.waitingQueueTokenRepository.saveToken(waitingQueueToken);
        }

        waitingQueueToken.validateWaiting();
        return waitingQueueToken;
    }

    public WaitingQueueToken validateAndGetActiveToken(String token) {
        var queueToken = this.waitingQueueTokenRepository.getTokenByTokenString(token);
    
        queueToken.validateActivation();
        return queueToken;
    }

    public void expireTokens() {
        Date tokenExpireStandard = new Date(System.currentTimeMillis() - expireDurationInMilli);
        List<WaitingQueueToken> tokensToExpire = waitingQueueTokenRepository.getActiveTokensActivatedAtBefore(tokenExpireStandard);

        if(tokensToExpire == null || tokensToExpire.isEmpty()) return;

        tokensToExpire.forEach(WaitingQueueToken::expire);
        waitingQueueTokenRepository.saveTokens(tokensToExpire);
    }

    public void expireToken(String token) {
        WaitingQueueToken waitingQueueToken = waitingQueueTokenRepository.getTokenByTokenString(token);
        waitingQueueToken.expire();
        waitingQueueTokenRepository.saveToken(waitingQueueToken);
    }
}
