package com.java.admin.mapper.user;

import com.java.admin.dto.user.response.GetUserResponseDto;
import com.java.admin.entity.user.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public GetUserResponseDto toGetUserResponseDto(UserEntity userEntity) {
        return new GetUserResponseDto(
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getPhoneNumber(),
                userEntity.getAddress()
        );
    }
}
