package io.hhplus.concert.domain.queue;

import io.hhplus.concert.support.exception.CustomBadRequestException;
import io.hhplus.concert.support.exception.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import io.hhplus.concert.support.exception.ExceptionCode;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final WaitingQueueTokenRepository waitingQueueTokenRepository;
    private final ActiveTokenRepository activeTokenRepository;

    public WaitingQueueToken getWaitingQueueToken(String token) {
        try{
            var activeToken = activeTokenRepository.getActiveTokenByTokenString(token);
            if(activeToken != null) {
                throw new CustomBadRequestException(ExceptionCode.TOKEN_IS_ACTIVATED);
            }
        } catch(CustomNotFoundException e) {
            if(!e.getCode().equals(ExceptionCode.ACTIVE_TOKEN_NOT_FOUND)) throw e;
        }

        try{
            return waitingQueueTokenRepository.getWaitingQueueTokenByTokenStr(token);
        } catch(CustomNotFoundException e) {
            if(!e.getCode().equals(ExceptionCode.WAITING_TOKEN_NOT_FOUND)) throw e;
        }

        WaitingQueueToken waitingQueueToken = new WaitingQueueToken();
        waitingQueueToken = this.waitingQueueTokenRepository.saveWaitingQueueToken(waitingQueueToken);
        return waitingQueueToken;
    }

    public ActiveToken getActiveToken(String tokenStr) {
        var activeToken = this.activeTokenRepository.getActiveTokenByTokenString(tokenStr);
        activeToken.renew();
        return activeToken;
    }

    public void expireToken(String token) {
        ActiveToken activeToken = activeTokenRepository.getActiveTokenByTokenString(token);
        activeTokenRepository.deleteActiveToken(activeToken);
    }
}
