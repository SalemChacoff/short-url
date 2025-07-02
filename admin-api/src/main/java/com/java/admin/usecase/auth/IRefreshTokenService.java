package com.java.admin.usecase.auth;

import com.java.admin.dto.auth.request.RefreshTokenRequestDto;
import com.java.admin.dto.auth.response.RefreshTokenResponseDto;
import com.java.admin.entity.auth.RefreshTokenEntity;

import java.util.Optional;

public interface IRefreshTokenService {

    RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto);
    RefreshTokenEntity createRefreshToken(String userEmail);
    Optional<RefreshTokenEntity> findByToken(String token);
    RefreshTokenEntity verifyExpiration(RefreshTokenEntity token);
    void deleteByUserEmail(String userEmail);
}
