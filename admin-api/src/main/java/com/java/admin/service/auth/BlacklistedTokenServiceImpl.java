package com.java.admin.service.auth;

import com.java.admin.entity.auth.BlacklistedTokenEntity;
import com.java.admin.repository.auth.BlacklistedTokenRepository;
import com.java.admin.usecase.auth.IBlacklistedTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class BlacklistedTokenServiceImpl implements IBlacklistedTokenService {

    private final BlacklistedTokenRepository blacklistedTokenRepository;

    public void blacklistToken(String token) {
        BlacklistedTokenEntity blacklistedTokenEntity = new BlacklistedTokenEntity();
        blacklistedTokenEntity.setToken(token);
        blacklistedTokenEntity.setExpiryAt(OffsetDateTime.now());

        blacklistedTokenRepository.save(blacklistedTokenEntity);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokenRepository.existsByToken(token);
    }

    public void cleanupExpiredTokens() { blacklistedTokenRepository.deleteByExpiryAtBefore(OffsetDateTime.now());}
}