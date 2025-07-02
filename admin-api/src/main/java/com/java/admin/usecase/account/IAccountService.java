package com.java.admin.usecase.account;

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

public interface IAccountService {

    CreateAccountResponseDto createAccount(CreateAccountRequestDto createAccountRequestDto);
    VerifyAccountResponseDto verifyAccount(VerifyAccountRequestDto verifyAccountRequestDto);
    ResendVerificationCodeAccountResponseDto resendVerificationCode(ResendVerificationCodeAccountRequestDto verifyAccountRequestDto);
    ValidateCodeAccountResponseDto validateCode(ValidateCodeAccountRequestDto validateCodeAccountRequestDto);
    ResetPasswordResponseDto resetPassword(ResetPasswordRequestDto resetPasswordRequestDto);
    ResetPasswordTokenResponseDto resetPasswordToken(ResetPasswordTokenRequestDto resetPasswordTokenRequestDto);
    ChangePasswordResponseDto changePassword(ChangePasswordRequestDto changePasswordRequestDto);
}
