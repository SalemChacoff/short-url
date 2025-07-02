package com.java.admin.service.auth;

import com.java.admin.config.CustomLogger;
import com.java.admin.dto.auth.request.LoginRequestDto;
import com.java.admin.dto.auth.request.LogoutRequestDto;
import com.java.admin.dto.auth.request.RefreshTokenRequestDto;
import com.java.admin.dto.auth.response.LoginResponseDto;
import com.java.admin.dto.auth.response.LogoutResponseDto;
import com.java.admin.dto.auth.response.RefreshTokenResponseDto;
import com.java.admin.entity.auth.RefreshTokenEntity;
import com.java.admin.exception.auth.AuthException;
import com.java.admin.security.JwtService;
import com.java.admin.usecase.auth.IAuthUserService;
import com.java.admin.usecase.auth.IBlacklistedTokenService;
import com.java.admin.usecase.auth.IRefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements IAuthUserService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailServiceImpl userDetailsService;
    private final JwtService jwtService;
    private final IRefreshTokenService refreshTokenService;
    private final IBlacklistedTokenService blacklistedTokenService;

    /**
     * This method handles user login by authenticating the user with the provided credentials.
     * If authentication is successful, it generates a JWT token and a refresh token.
     *
     * @param loginRequestDto contains the user's email and password
     * @return LoginResponseDto containing the JWT token and refresh token
     */
    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication userAuth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.email(),
                        loginRequestDto.password()
                )
        );

        if (!userAuth.isAuthenticated()) {
            CustomLogger.logInfo(AuthUserServiceImpl.class, "Authentication failed for user: " + loginRequestDto.email());
            throw new AuthException(
                    AuthException.AUTHENTICATION_FAILED_CODE,
                    AuthException.AUTHENTICATION_FAILED_MESSAGE,
                    AuthException.AUTHENTICATION_FAILED_CAUSE
            );
        }

        CustomLogger.logInfo(AuthUserServiceImpl.class, "User authenticated successfully, generating JWT token for email: " + loginRequestDto.email());
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDto.email());
        String jwtToken = jwtService.generateToken(userDetails);
        RefreshTokenEntity refreshTokenEntity = refreshTokenService.createRefreshToken(loginRequestDto.email());

        return new LoginResponseDto(
                jwtToken,
                refreshTokenEntity.getToken()
        );
    }

    /**
     * This method handles user logout by blacklisting the JWT token and deleting the associated refresh token.
     *
     * @param logoutRequestDto contains the JWT token to be blacklisted
     * @return LogoutResponseDto indicating successful logout
     */
    @Override
    public LogoutResponseDto logout(LogoutRequestDto logoutRequestDto) {

        if (logoutRequestDto.token() != null && logoutRequestDto.token().startsWith("Bearer ")) {
            String jwt = logoutRequestDto.token().substring(7);
            blacklistedTokenService.blacklistToken(jwt);

            String userEmail = jwtService.extractUsername(jwt);
            refreshTokenService.deleteByUserEmail(userEmail);

            return new LogoutResponseDto(
                    "User logged out successfully and token blacklisted."
            );
        }

        CustomLogger.logWarning(AuthUserServiceImpl.class, "Logout failed: Invalid token format or missing token");

        throw new AuthException(
                AuthException.INVALID_TOKEN_CODE,
                AuthException.INVALID_TOKEN_MESSAGE,
                AuthException.INVALID_TOKEN_CAUSE
        );
    }

    /**
     * This method refreshes the JWT token using a valid refresh token.
     * It checks if the refresh token is valid and not expired, then generates a new JWT token.
     *
     * @param refreshTokenRequestDto contains the refresh token
     * @return RefreshTokenResponseDto containing the new JWT token and refresh token
     */
    @Override
    public RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto) {

        RefreshTokenEntity refreshTokenEntity = refreshTokenService.findByToken(refreshTokenRequestDto.refreshToken()).orElse(null);

        if (refreshTokenEntity == null || refreshTokenEntity.isExpired()) {
            CustomLogger.logWarning(AuthUserServiceImpl.class, "Refresh token is invalid or expired for token: " + refreshTokenRequestDto.refreshToken());
            throw new AuthException(
                    AuthException.REFRESH_TOKEN_INVALID_CODE,
                    AuthException.REFRESH_TOKEN_INVALID_MESSAGE,
                    AuthException.REFRESH_TOKEN_INVALID_CAUSE
            );
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(refreshTokenEntity.getUserEmail());
        String token = jwtService.generateToken(userDetails);
        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(refreshTokenEntity.getUserEmail());

        // Delete the old refresh token
        refreshTokenService.deleteByUserEmail(refreshTokenEntity.getUserEmail());

        return new RefreshTokenResponseDto(
                token,
                refreshToken.getToken());

    }

}
