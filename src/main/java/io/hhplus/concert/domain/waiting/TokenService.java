package io.hhplus.concert.domain.waiting;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class TokenService {
    private final long expireDurationInMilli = 30 * 60 * 1000l;

    WaitingTokenRepository waitingTokenRepository;

    public TokenService(WaitingTokenRepository waitingTokenRepository) {
        this.waitingTokenRepository = waitingTokenRepository;
    }

    public WaitingToken getToken(long userId) {
        var waitingToken = waitingTokenRepository.getTokenByUserId(userId);
        if(waitingToken != null) return waitingToken;

        waitingToken = new WaitingToken(userId);
        return this.waitingTokenRepository.saveToken(waitingToken);
    }

    public WaitingToken validateAndGetActiveToken(String token) {
        var waitingToken = this.waitingTokenRepository.getTokenByTokenString(token);
        waitingToken.validateActivation();
        return waitingToken;
    }

    @Transactional
    public void expireTokens() {
        Date tokenExpireStandard = new Date(System.currentTimeMillis() - expireDurationInMilli);
        List<WaitingToken> tokensToExpire = waitingTokenRepository.getActiveTokensActivatedAtBefore(tokenExpireStandard);

        if(tokensToExpire == null || tokensToExpire.isEmpty()) return;

        tokensToExpire.forEach(WaitingToken::expire);
        waitingTokenRepository.saveAllTokens(tokensToExpire);
    }

    public void expireToken(String token) {
        WaitingToken waitingToken = waitingTokenRepository.getTokenByTokenString(token);
        waitingToken.expire();
        waitingTokenRepository.saveToken(waitingToken);
    }
}
