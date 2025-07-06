package com.java.admin.controller.auth;

import com.java.admin.config.CustomLogger;
import com.java.admin.constant.ApiAuthEndpoints;
import com.java.admin.dto.ApiResponseDto;
import com.java.admin.dto.auth.request.LoginRequestDto;
import com.java.admin.dto.auth.request.LogoutRequestDto;
import com.java.admin.dto.auth.response.LoginResponseDto;
import com.java.admin.dto.auth.request.RefreshTokenRequestDto;
import com.java.admin.usecase.auth.IAuthUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping(ApiAuthEndpoints.BASE_PATH)
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoint for user authentication")
@Slf4j
public class AuthenticationController {

    private final IAuthUserService authUserService;

    @Operation(
            summary = "Login User",
            description = "Endpoint to authenticate a user and return a JWT token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful authentication",
                            content = @Content(schema = @Schema(implementation = LoginResponseDto.class,
                                    example = "{\"token\": \"your_jwt_token_here\"}"))),
                    @ApiResponse(responseCode = "401", description = "Bad credentials",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class,
                                    example = "{\"errors\": [{\"verificationCode\": 401, \"message\": \"Unauthorized\", \"description\": \"Invalid email or password\"}], \"success\": false, \"data\": null}")))
            }
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto> authenticate(
            @Valid @RequestBody LoginRequestDto request
    ) {

        CustomLogger.logInfo(AuthenticationController.class, "User login attempt for email: " + request.email());

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponseDto(
                        new ArrayList<>(),
                        true,
                        authUserService.login(request))
        );
    }


    @Operation(
            summary = "Logout User",
            description = "Endpoint to logout a user and invalidate the JWT token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful logout",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class,
                                    example = "{\"errors\": [], \"success\": true, \"data\": \"User logged out successfully\"}"))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class,
                                    example = "{\"errors\": [{\"verificationCode\": 401, \"message\": \"Unauthorized\", \"description\": \"Invalid token\"}], \"success\": false, \"data\": null}")))
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto> logout(@RequestHeader("Authorization") String authHeader) {
        LogoutRequestDto logoutRequestDto = new LogoutRequestDto(authHeader);

        CustomLogger.logInfo(AuthenticationController.class, "User logout attempt with token: " + logoutRequestDto.token());

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponseDto(
                        new ArrayList<>(),
                        true,
                        authUserService.logout(logoutRequestDto)
                )
        );
    }

    @Operation(
            summary = "Refresh Token",
            description = "Endpoint to refresh the JWT token using a refresh token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token refreshed successfully",
                            content = @Content(schema = @Schema(implementation = LoginResponseDto.class,
                                    example = "{\"token\": \"new_jwt_token_here\"}"))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class,
                                    example = "{\"errors\": [{\"verificationCode\": 401, \"message\": \"Unauthorized\", \"description\": \"Invalid refresh token\"}], \"success\": false, \"data\": null}")))
            }
    )
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponseDto> refreshToken(@Valid @RequestBody RefreshTokenRequestDto request) {

        CustomLogger.logInfo(AuthenticationController.class, "Refresh token request received for token: " + request.refreshToken());

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponseDto(
                        new ArrayList<>(),
                        true,
                        authUserService.refreshToken(request)
                )
        );
    }
}
