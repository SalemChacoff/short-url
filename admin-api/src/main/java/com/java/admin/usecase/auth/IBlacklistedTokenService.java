package com.java.admin.usecase.auth;

public interface IBlacklistedTokenService {

    void blacklistToken(String token);
    boolean isTokenBlacklisted(String token);
    void cleanupExpiredTokens();
}
