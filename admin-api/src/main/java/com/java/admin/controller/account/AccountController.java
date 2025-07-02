package com.java.admin.controller.account;

import com.java.admin.config.CustomLogger;
import com.java.admin.constant.ApiAccountEndpoints;
import com.java.admin.dto.ApiResponseDto;
import com.java.admin.dto.account.request.ChangePasswordRequestDto;
import com.java.admin.dto.account.request.CreateAccountRequestDto;
import com.java.admin.dto.account.request.ResendVerificationCodeAccountRequestDto;
import com.java.admin.dto.account.request.ResetPasswordRequestDto;
import com.java.admin.dto.account.request.ResetPasswordTokenRequestDto;
import com.java.admin.dto.account.request.ValidateCodeAccountRequestDto;
import com.java.admin.dto.account.request.VerifyAccountRequestDto;
import com.java.admin.usecase.account.IAccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping(ApiAccountEndpoints.BASE_PATH)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Account", description = "Endpoints for user account management")
public class AccountController {

    private final IAccountService accountService;

    @Tag(name = "Account", description = "Endpoints para la gestión de usuarios")
    @PostMapping(
            value = ApiAccountEndpoints.CREATE_ACCOUNT,
            produces = "application/json",
            consumes = "application/json")
    public ResponseEntity<ApiResponseDto> createUserAccount(@Valid @RequestBody CreateAccountRequestDto createUserRequestDto) {
        log.info("Creating user account with email: {}", createUserRequestDto.email());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponseDto(new ArrayList<>(),
                        true,
                        accountService.createAccount(createUserRequestDto)));
    }

    @GetMapping(
            value = ApiAccountEndpoints.VERIFY_ACCOUNT,
            produces = "application/json")
    public ResponseEntity<ApiResponseDto> verifyAccount(@PathVariable String token) {
        VerifyAccountRequestDto verificationToken = new VerifyAccountRequestDto(token);
        CustomLogger.logInfo(AccountController.class, "Verifying account with token: " + token);

        return ResponseEntity.ok(new ApiResponseDto(new ArrayList<>(),
                true,
                accountService.verifyAccount(verificationToken)));
    }

    @PostMapping(
            value = ApiAccountEndpoints.RESEND_VERIFICATION_CODE,
            produces = "application/json")
    public ResponseEntity<ApiResponseDto> resendVerificationCode(@Valid @RequestBody ResendVerificationCodeAccountRequestDto email) {
        CustomLogger.logInfo(AccountController.class, "Resending verification code to email: " + email.email());

        return ResponseEntity.ok(new ApiResponseDto(new ArrayList<>(),
                true,
                accountService.resendVerificationCode(email)));
    }

    @PostMapping(
            value = ApiAccountEndpoints.VALIDATE_CODE,
            produces = "application/json")
    public ResponseEntity<ApiResponseDto> validateCode(@Valid @RequestBody ValidateCodeAccountRequestDto validateCodeAccountRequestDto) {
        CustomLogger.logInfo(AccountController.class, "Validate code with email" + validateCodeAccountRequestDto.email());
        return ResponseEntity.ok(new ApiResponseDto(new ArrayList<>(),
                true,
                accountService.validateCode(validateCodeAccountRequestDto)));
    }

    @PostMapping(
            value = ApiAccountEndpoints.RESET_PASSWORD,
            produces = "application/json",
            consumes = "application/json")
    public ResponseEntity<ApiResponseDto> resetPassword(@Valid @RequestBody ResetPasswordRequestDto email) {
        CustomLogger.logInfo(AccountController.class, "Resetting password for email: " + email.email());
        return ResponseEntity.ok(new ApiResponseDto(new ArrayList<>(),
                true,
                accountService.resetPassword(email)));
    }

    @GetMapping(
            value = ApiAccountEndpoints.VALIDATE_RESET_PASSWORD,
            produces = "application/json")
    public ResponseEntity<ApiResponseDto> validateResetPasswordToken(@PathVariable String token) {
        ResetPasswordTokenRequestDto resetPasswordToken = new ResetPasswordTokenRequestDto(token);
        CustomLogger.logInfo(AccountController.class, "Validating reset password token: " + resetPasswordToken.resetPasswordToken());
        return ResponseEntity.ok(new ApiResponseDto(new ArrayList<>(),
                true,
                accountService.resetPasswordToken(resetPasswordToken)));
    }

    @PostMapping(
            value = ApiAccountEndpoints.CHANGE_PASSWORD,
            produces = "application/json",
            consumes = "application/json")
    public ResponseEntity<ApiResponseDto> changePassword(@Valid @RequestBody ChangePasswordRequestDto changePasswordRequest) {
        CustomLogger.logInfo(AccountController.class, "Changing password for user with email: " + changePasswordRequest.email());
        return ResponseEntity.ok(new ApiResponseDto(new ArrayList<>(),
                true,
                accountService.changePassword(changePasswordRequest)));
    }

}