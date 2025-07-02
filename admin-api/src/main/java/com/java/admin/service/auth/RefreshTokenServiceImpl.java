package com.java.admin.service.auth;

import com.java.admin.config.CustomLogger;
import com.java.admin.config.JwtPropertiesConfig;
import com.java.admin.dto.auth.request.RefreshTokenRequestDto;
import com.java.admin.dto.auth.response.RefreshTokenResponseDto;
import com.java.admin.entity.auth.RefreshTokenEntity;
import com.java.admin.exception.auth.AuthException;
import com.java.admin.repository.auth.RefreshTokenRepository;
import com.java.admin.security.JwtService;
import com.java.admin.usecase.auth.IRefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements IRefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtPropertiesConfig jwtPropertiesConfig;
    private final UserDetailServiceImpl userDetailsService;
    private final JwtService jwtService;

    public RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto) {

        RefreshTokenEntity refreshTokenEntity = findByToken(refreshTokenRequestDto.refreshToken()).orElse(null);

        if (refreshTokenEntity == null) {
            CustomLogger.logWarning(RefreshTokenServiceImpl.class, "Refresh token not found: " + refreshTokenRequestDto.refreshToken());
            throw new AuthException(
                    AuthException.REFRESH_TOKEN_INVALID_CODE,
                    AuthException.REFRESH_TOKEN_INVALID_MESSAGE,
                    AuthException.REFRESH_TOKEN_INVALID_CAUSE
            );
        }

        refreshTokenEntity = verifyExpiration(refreshTokenEntity);

        UserDetails userDetails = userDetailsService.loadUserByUsername(refreshTokenEntity.getUserEmail());
        String token = jwtService.generateToken(userDetails);
        RefreshTokenEntity refreshToken = createRefreshToken(refreshTokenEntity.getUserEmail());

        // Delete the old refresh token
        refreshTokenRepository.delete(refreshTokenEntity);

        return new RefreshTokenResponseDto(
                        token,
                        refreshToken.getToken());

    }

    public RefreshTokenEntity createRefreshToken(String userEmail) {
        refreshTokenRepository.findByUserEmail(userEmail).ifPresent(refreshTokenRepository::delete);

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setUserEmail(userEmail);
        refreshTokenEntity.setToken(UUID.randomUUID().toString());
        refreshTokenEntity.setExpiryAt(Instant.now().plusMillis(jwtPropertiesConfig.getRefreshTokenExpiration()).atOffset(OffsetDateTime.now().getOffset()));

        return refreshTokenRepository.save(refreshTokenEntity);
    }

    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
        if (token.isExpired()) {
            CustomLogger.logWarning(RefreshTokenServiceImpl.class, "Refresh token is expired: " + token.getToken());
            refreshTokenRepository.delete(token);
            throw new AuthException(
                    AuthException.REFRESH_TOKEN_INVALID_CODE,
                    AuthException.REFRESH_TOKEN_INVALID_MESSAGE,
                    AuthException.REFRESH_TOKEN_INVALID_CAUSE
            );
        }
        return token;
    }

    @Override
    @Transactional
    public void deleteByUserEmail(String userEmail) {
        refreshTokenRepository.deleteByUserEmail(userEmail);
    }
}
