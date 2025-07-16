package com.java.admin.usecase.user;

import com.java.admin.dto.user.request.UpdateUserRequestDto;
import com.java.admin.dto.user.response.GetUserResponseDto;
import com.java.admin.dto.user.response.UpdateUserResponseDto;

public interface IUserService {

    GetUserResponseDto getUserById(Long userId);
    UpdateUserResponseDto updateUser(Long userId, UpdateUserRequestDto updateUserRequestDto);

}
