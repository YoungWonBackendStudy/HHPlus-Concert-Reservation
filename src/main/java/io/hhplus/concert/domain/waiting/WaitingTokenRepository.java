package io.hhplus.concert.domain.waiting;

import java.util.Date;
import java.util.List;

import io.hhplus.concert.domain.waiting.WaitingToken.TokenStatus;

public interface WaitingTokenRepository {
    public WaitingToken saveToken(WaitingToken waitingToken);
    public List<WaitingToken> saveAllTokens(List<WaitingToken> waitingTokens);

    public WaitingToken getTokenByUserId(long userId);
    public WaitingToken getTokenByTokenString(String token);

    public WaitingToken getLastTokenByStatus(TokenStatus tokenStatus);
    public List<WaitingToken> getTokensByStatus(TokenStatus tokenStatus);
    public List<WaitingToken> getTokensByStatusAndSize(TokenStatus tokenStatus, long size);

    public List<WaitingToken> getActiveTokensActivatedAtBefore(Date date);
}
