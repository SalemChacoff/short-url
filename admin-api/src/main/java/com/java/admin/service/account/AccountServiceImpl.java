package com.java.admin.service.account;

import com.java.admin.config.CustomLogger;
import com.java.admin.config.ServicePropertiesConfig;
import com.java.admin.constant.ApiAccountEndpoints;
import com.java.admin.dto.account.request.ChangePasswordRequestDto;
import com.java.admin.dto.account.request.CreateAccountRequestDto;
import com.java.admin.dto.account.request.ResendVerificationCodeAccountRequestDto;
import com.java.admin.dto.account.request.ResetPasswordRequestDto;
import com.java.admin.dto.account.request.ResetPasswordTokenRequestDto;
import com.java.admin.dto.account.request.ValidateCodeAccountRequestDto;
import com.java.admin.dto.account.request.VerifyAccountRequestDto;
import com.java.admin.dto.account.response.ChangePasswordResponseDto;
import com.java.admin.dto.account.response.CreateAccountResponseDto;
import com.java.admin.dto.account.response.ResendVerificationCodeAccountResponseDto;
import com.java.admin.dto.account.response.ResetPasswordResponseDto;
import com.java.admin.dto.account.response.ResetPasswordTokenResponseDto;
import com.java.admin.dto.account.response.ValidateCodeAccountResponseDto;
import com.java.admin.dto.account.response.VerifyAccountResponseDto;
import com.java.admin.entity.user.UserEntity;
import com.java.admin.exception.account.AccountException;
import com.java.admin.mapper.account.AccountMapper;
import com.java.admin.repository.user.UserRepository;
import com.java.admin.security.AuthProvider;
import com.java.admin.service.user.UserServiceImpl;
import com.java.admin.usecase.account.IAccountService;
import com.java.admin.util.GenerateRandomDataUtil;
import com.java.admin.util.SendMailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final UserRepository userRepository;
    private final SendMailUtil sendMailUtil;
    private final AccountMapper accountMapper;
    private final ServicePropertiesConfig servicePropertiesConfig;


    @Override
    public CreateAccountResponseDto createAccount(CreateAccountRequestDto createAccountRequestDto) {

        boolean userAccountExist = userRepository.existsUserEntityByEmail(createAccountRequestDto.email());

        if (userAccountExist) {
            CustomLogger.logWarning(UserServiceImpl.class,
                    "User account already exists with email: " + createAccountRequestDto.email());
            throw new AccountException(
                    AccountException.USER_ACCOUNT_ALREADY_EXISTS_CODE,
                    AccountException.USER_ACCOUNT_ALREADY_EXISTS_MESSAGE,
                    AccountException.USER_ACCOUNT_ALREADY_EXISTS_CAUSE
            );
        }

        UserEntity userEntity = accountMapper.toEntity(createAccountRequestDto, AuthProvider.LOCAL);
        userEntity.setCredentialsNonExpired(true);
        userEntity.setAccountNonExpired(true);
        userEntity.setAccountNonLocked(true);
        userEntity.setEnabled(false);

        String verificationToken = UUID.randomUUID().toString();
        String verificationCode = GenerateRandomDataUtil.generateCode();
        OffsetDateTime verificationTokenExpiry = OffsetDateTime.now().plusDays(1);
        userEntity.setVerificationToken(verificationToken);
        userEntity.setVerificationTokenExpiry(verificationTokenExpiry);
        userEntity.setVerificationCode(verificationCode);
        userEntity.setMaxVerificationCodeAttempts(0);
        userEntity.setMaxResetPasswordAttempts(0);
        String verificationUrl = servicePropertiesConfig.getBaseHostPath() + "/api/v1/account/verification/" + verificationToken;


        // Set the password using the password encoder
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userEntity.setPassword(passwordEncoder.encode(createAccountRequestDto.password()));
        userEntity = userRepository.save(userEntity);

        UserEntity finalUserEntity = userEntity;
        sendVerificationMail(verificationUrl, finalUserEntity);

        return new CreateAccountResponseDto(
                "User account created successfully with email: " + userEntity.getEmail()
        );
    }

    @Override
    public VerifyAccountResponseDto verifyAccount(VerifyAccountRequestDto verifyAccountRequestDto) {

        UserEntity userEntity = userRepository.findUserEntityByVerificationToken(verifyAccountRequestDto.verificationToken()).orElse(null);

        if (userEntity == null) {
            CustomLogger.logWarning(AccountServiceImpl.class,
                    "User account not found with verification token: " + verifyAccountRequestDto.verificationToken());
            throw new AccountException(
                    AccountException.USER_ACCOUNT_NOT_FOUND_CODE,
                    AccountException.USER_ACCOUNT_NOT_FOUND_MESSAGE,
                    AccountException.USER_ACCOUNT_NOT_FOUND_CAUSE
            );
        }

        // Check if the verification token is expired
        if (userEntity.getVerificationTokenExpiry().isBefore(OffsetDateTime.now())) {
            CustomLogger.logWarning(AccountServiceImpl.class,
                    "Verification token expired for user account with email: " + userEntity.getEmail());
            throw new AccountException(
                    AccountException.VERIFICATION_TOKEN_EXPIRED_CODE,
                    AccountException.VERIFICATION_TOKEN_EXPIRED_MESSAGE,
                    AccountException.VERIFICATION_TOKEN_EXPIRED_CAUSE
            );
        }

        return new VerifyAccountResponseDto(
                "Token verified is still valid for user account with email: " + userEntity.getEmail(),
                verifyAccountRequestDto.verificationToken()
        );
    }

    @Override
    public ResendVerificationCodeAccountResponseDto resendVerificationCode(ResendVerificationCodeAccountRequestDto verifyAccountRequestDto) {

        UserEntity userEntity = userRepository.findUserEntityByEmail(verifyAccountRequestDto.email()).orElse(null);

        if (userEntity == null) {
            CustomLogger.logWarning(AccountServiceImpl.class,
                    "User account not found with this email: " + verifyAccountRequestDto.email());
            throw new AccountException(
                    AccountException.USER_ACCOUNT_NOT_FOUND_CODE,
                    AccountException.USER_ACCOUNT_NOT_FOUND_MESSAGE,
                    AccountException.USER_ACCOUNT_NOT_FOUND_CAUSE
            );
        }

        if (userEntity.isEnabled()) {
            CustomLogger.logWarning(AccountServiceImpl.class,
                    "User account with the email: " + verifyAccountRequestDto.email() + " is already enabled.");
            throw new AccountException(
                    AccountException.USER_ACCOUNT_ALREADY_ENABLED_CODE,
                    AccountException.USER_ACCOUNT_ALREADY_ENABLED_MESSAGE,
                    AccountException.USER_ACCOUNT_ALREADY_ENABLED_CAUSE
            );
        }

        String verificationToken = UUID.randomUUID().toString();
        String verificationCode = GenerateRandomDataUtil.generateCode();
        OffsetDateTime verificationTokenExpiry = OffsetDateTime.now().plusDays(1);
        userEntity.setVerificationToken(verificationToken);
        userEntity.setVerificationTokenExpiry(verificationTokenExpiry);
        userEntity.setVerificationCode(verificationCode);
        userRepository.save(userEntity);

        String verificationUrl = servicePropertiesConfig.getBaseHostPath() +
                ApiAccountEndpoints.BASE_PATH +
                ApiAccountEndpoints.VERIFY_ACCOUNT + "/" + verificationToken;

        // Enviar correo con el código de verificación
        sendVerificationMail(verificationUrl, userEntity);

        return new ResendVerificationCodeAccountResponseDto(
                "Verification verificationCode resent successfully to email: " + userEntity.getEmail()
        );
    }

    @Override
    public ValidateCodeAccountResponseDto validateCode(ValidateCodeAccountRequestDto validateCodeAccountRequestDto) {

        UserEntity userEntity = userRepository.findUserEntityByEmail(validateCodeAccountRequestDto.email()).orElse(null);

        if (userEntity == null) {
            CustomLogger.logWarning(AccountServiceImpl.class,
                    "User account not found with the email: " + validateCodeAccountRequestDto.email());
            throw new AccountException(
                    AccountException.USER_ACCOUNT_NOT_FOUND_CODE,
                    AccountException.USER_ACCOUNT_NOT_FOUND_MESSAGE,
                    AccountException.USER_ACCOUNT_NOT_FOUND_CAUSE
            );
        }

        // Validate if the verification token is still valid
        if (userEntity.getVerificationTokenExpiry().isBefore(OffsetDateTime.now())) {
            CustomLogger.logWarning(AccountServiceImpl.class,
                    "Verification token expired for user account with email: " + validateCodeAccountRequestDto.email());
            throw new AccountException(
                    AccountException.VERIFICATION_TOKEN_EXPIRED_CODE,
                    AccountException.VERIFICATION_TOKEN_EXPIRED_MESSAGE,
                    AccountException.VERIFICATION_TOKEN_EXPIRED_CAUSE
            );
        }

        if (userEntity.getMaxVerificationCodeAttempts() >= servicePropertiesConfig.getMaxVerificationCodeAttempts()) {
            CustomLogger.logWarning(AccountServiceImpl.class,
                    "Maximum verification verificationCode attempts reached for user account with email: " + validateCodeAccountRequestDto.email());
            throw new AccountException(
                    AccountException.MAX_VERIFICATION_CODE_ATTEMPTS_REACHED_CODE,
                    AccountException.MAX_VERIFICATION_CODE_ATTEMPTS_REACHED_MESSAGE,
                    AccountException.MAX_VERIFICATION_CODE_ATTEMPTS_REACHED_CAUSE
            );
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean isCodeValid = userEntity.getVerificationCode().equals(validateCodeAccountRequestDto.verificationCode());
        boolean isPasswordValid = passwordEncoder.matches(validateCodeAccountRequestDto.password(), userEntity.getPassword());

        if (!isCodeValid || !isPasswordValid) {
            CustomLogger.logWarning(AccountServiceImpl.class,
                    "Invalid verification verificationCode or password for user account with email: " + validateCodeAccountRequestDto.email());

            userEntity.setMaxVerificationCodeAttempts(userEntity.getMaxVerificationCodeAttempts() + 1);
            userRepository.save(userEntity);


            throw new AccountException(
                    AccountException.INVALID_VERIFICATION_CODE_OR_PASSWORD_CODE,
                    AccountException.INVALID_VERIFICATION_CODE_OR_PASSWORD_MESSAGE,
                    AccountException.INVALID_VERIFICATION_CODE_OR_PASSWORD_CAUSE
            );
        }

        // If all validations pass, enable the user account
        userEntity.setEnabled(true);
        userEntity.setVerificationToken(null);
        userEntity.setVerificationTokenExpiry(null);
        userEntity.setVerificationCode(null);
        userEntity.setMaxVerificationCodeAttempts(0);
        userRepository.save(userEntity);

        return new ValidateCodeAccountResponseDto(
                "User account with email: " + validateCodeAccountRequestDto.email() + " has been successfully validated and enabled."
        );
    }

    @Override
    public ResetPasswordResponseDto resetPassword(ResetPasswordRequestDto resetPasswordRequestDto) {

        UserEntity userEntity = userRepository.findUserEntityByEmail(resetPasswordRequestDto.email()).orElse(null);

        if (userEntity == null) {
            CustomLogger.logWarning(AccountServiceImpl.class,
                    "User account not found with email: " + resetPasswordRequestDto.email());
            throw new AccountException(
                    AccountException.USER_ACCOUNT_NOT_FOUND_CODE,
                    AccountException.USER_ACCOUNT_NOT_FOUND_MESSAGE,
                    AccountException.USER_ACCOUNT_NOT_FOUND_CAUSE
            );
        }

        if (!userEntity.isEnabled()) {
            CustomLogger.logWarning(AccountServiceImpl.class,
                    "User account with email: " + resetPasswordRequestDto.email() + " is not enabled.");
            throw new AccountException(
                    AccountException.USER_ACCOUNT_NOT_ENABLED_CODE,
                    AccountException.USER_ACCOUNT_NOT_ENABLED_MESSAGE,
                    AccountException.USER_ACCOUNT_NOT_ENABLED_CAUSE
            );
        }

        if (userEntity.getMaxResetPasswordAttempts() >= servicePropertiesConfig.getMaxResetPasswordAttempts()) {
            CustomLogger.logWarning(AccountServiceImpl.class,
                    "Maximum reset password attempts reached for user account with email: " + resetPasswordRequestDto.email());
            throw new AccountException(
                    AccountException.MAX_RESET_PASSWORD_ATTEMPTS_REACHED_CODE,
                    AccountException.MAX_RESET_PASSWORD_ATTEMPTS_REACHED_MESSAGE,
                    AccountException.MAX_RESET_PASSWORD_ATTEMPTS_REACHED_CAUSE
            );
        }


        String resetPasswordCode = GenerateRandomDataUtil.generateCode();
        String resetPasswordToken = UUID.randomUUID().toString();
        OffsetDateTime resetPasswordTokenExpiry = OffsetDateTime.now().plusHours(1);
        userEntity.setResetPasswordToken(resetPasswordToken);
        userEntity.setResetPasswordTokenExpiry(resetPasswordTokenExpiry);
        userEntity.setResetPasswordCode(resetPasswordCode);
        userRepository.save(userEntity);

        // Crear URL para recuperación de contraseña
        String resetUrl =  servicePropertiesConfig.getBaseHostPath() +
                ApiAccountEndpoints.BASE_PATH +
                ApiAccountEndpoints.RESET_PASSWORD + "/" + resetPasswordToken;

        // Enviar correo con código de recuperación
        Map<String, Object> vars = new HashMap<>();
        vars.put("userName", userEntity.getFirstName());
        vars.put("resetCode", resetPasswordCode);
        vars.put("resetUrl", resetUrl);

        // Enviar correo en un hilo separado para no bloquear la respuesta
        new Thread(() -> sendMailUtil.sendHtmlMessage(
                userEntity.getEmail(),
                "Reset Your Password",
                servicePropertiesConfig.getEmailResetPasswordTemplateName(),
                vars
        )).start();

        return new ResetPasswordResponseDto(
                "Reset password verificationCode sent to email: " + userEntity.getEmail()
        );
    }

    @Override
    public ResetPasswordTokenResponseDto resetPasswordToken(ResetPasswordTokenRequestDto resetPasswordTokenRequestDto) {

        UserEntity userEntity = userRepository.findUserEntityByResetPasswordToken(resetPasswordTokenRequestDto.resetPasswordToken()).orElse(null);

        if (userEntity == null) {
            CustomLogger.logWarning(AccountServiceImpl.class,
                    "User account not found with reset password token: " + resetPasswordTokenRequestDto.resetPasswordToken());
            throw new AccountException(
                    AccountException.USER_ACCOUNT_NOT_FOUND_CODE,
                    AccountException.USER_ACCOUNT_NOT_FOUND_MESSAGE,
                    AccountException.USER_ACCOUNT_NOT_FOUND_CAUSE
            );
        }

        // Check if the reset password token is expired
        if (userEntity.getResetPasswordTokenExpiry().isBefore(OffsetDateTime.now())) {
            CustomLogger.logWarning(AccountServiceImpl.class,
                    "Reset password token expired for user account with email: " + userEntity.getEmail());
            throw new AccountException(
                    AccountException.RESET_PASSWORD_TOKEN_EXPIRED_CODE,
                    AccountException.RESET_PASSWORD_TOKEN_EXPIRED_MESSAGE,
                    AccountException.RESET_PASSWORD_TOKEN_EXPIRED_CAUSE
            );
        }

        return new ResetPasswordTokenResponseDto(
                "Reset password token is still valid for user account with email: " + userEntity.getEmail(),
                resetPasswordTokenRequestDto.resetPasswordToken()
        );

    }

    @Override
    public ChangePasswordResponseDto changePassword(ChangePasswordRequestDto changePasswordRequestDto) {

        UserEntity userEntity = userRepository.findUserEntityByEmail(changePasswordRequestDto.email()).orElse(null);
        if (userEntity == null) {
            CustomLogger.logWarning(AccountServiceImpl.class,
                    "User account not found with email: " + changePasswordRequestDto.email());
            throw new AccountException(
                    AccountException.USER_ACCOUNT_NOT_FOUND_CODE,
                    AccountException.USER_ACCOUNT_NOT_FOUND_MESSAGE,
                    AccountException.USER_ACCOUNT_NOT_FOUND_CAUSE
            );
        }

        // Validate if the reset password token is still valid
        if (userEntity.getResetPasswordTokenExpiry().isBefore(OffsetDateTime.now())) {
            CustomLogger.logWarning(AccountServiceImpl.class,
                    "Reset password token expired for user account with email: " + changePasswordRequestDto.email());
            throw new AccountException(
                    AccountException.RESET_PASSWORD_TOKEN_EXPIRED_CODE,
                    AccountException.RESET_PASSWORD_TOKEN_EXPIRED_MESSAGE,
                    AccountException.RESET_PASSWORD_TOKEN_EXPIRED_CAUSE
            );
        }

        if (userEntity.getMaxResetPasswordAttempts() >= servicePropertiesConfig.getMaxResetPasswordAttempts()) {
            CustomLogger.logWarning(AccountServiceImpl.class,
                    "Maximum reset password attempts reached for user account with email: " + changePasswordRequestDto.email());
            throw new AccountException(
                    AccountException.MAX_RESET_PASSWORD_ATTEMPTS_REACHED_CODE,
                    AccountException.MAX_RESET_PASSWORD_ATTEMPTS_REACHED_MESSAGE,
                    AccountException.MAX_RESET_PASSWORD_ATTEMPTS_REACHED_CAUSE
            );
        }

        boolean isResetPasswordCodeValid = userEntity.getResetPasswordCode().equals(changePasswordRequestDto.resetPasswordCode());

        if (!isResetPasswordCodeValid) {
            CustomLogger.logWarning(AccountServiceImpl.class,
                    "Invalid reset password verificationCode or current password for user account with email: " + changePasswordRequestDto.email());

            userEntity.setMaxResetPasswordAttempts(userEntity.getMaxResetPasswordAttempts() + 1);
            userRepository.save(userEntity);

            throw new AccountException(
                    AccountException.INVALID_RESET_PASSWORD_CODE_OR_CURRENT_PASSWORD_CODE,
                    AccountException.INVALID_RESET_PASSWORD_CODE_OR_CURRENT_PASSWORD_MESSAGE,
                    AccountException.INVALID_RESET_PASSWORD_CODE_OR_CURRENT_PASSWORD_CAUSE
            );
        }

        // If the reset password verificationCode is valid, update the password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userEntity.setPassword(passwordEncoder.encode(changePasswordRequestDto.newPassword()));
        userEntity.setResetPasswordToken(null);
        userEntity.setResetPasswordTokenExpiry(null);
        userEntity.setResetPasswordCode(null);
        userEntity.setMaxResetPasswordAttempts(0);
        userRepository.save(userEntity);


        return new ChangePasswordResponseDto(
                "Password changed successfully for user account with email: " + changePasswordRequestDto.email()
        );
    }

    private void sendVerificationMail(String verificationUrl, UserEntity finalUserEntity) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("userName", finalUserEntity.getFirstName());
        vars.put("verificationCode", finalUserEntity.getVerificationCode());
        vars.put("verificationUrl", verificationUrl);

        new Thread(() -> sendMailUtil.sendHtmlMessage(
                finalUserEntity.getEmail(),
                "Welcome to Our Service - Email Verification",
                servicePropertiesConfig.getEmailVerifyTemplateName(),
                vars
        )).start();
    }

}
