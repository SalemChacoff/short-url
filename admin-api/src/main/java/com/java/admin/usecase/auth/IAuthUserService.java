package com.java.admin.usecase.auth;

import com.java.admin.dto.auth.request.LoginRequestDto;
import com.java.admin.dto.auth.request.LogoutRequestDto;
import com.java.admin.dto.auth.request.RefreshTokenRequestDto;
import com.java.admin.dto.auth.response.LoginResponseDto;
import com.java.admin.dto.auth.response.LogoutResponseDto;
import com.java.admin.dto.auth.response.RefreshTokenResponseDto;

public interface IAuthUserService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);
    LogoutResponseDto logout(LogoutRequestDto logoutRequestDto);
    RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto);
}
