package com.java.admin.mapper.user;

import com.java.admin.dto.user.request.CreateUserRequestDto;
import com.java.admin.dto.user.response.UserAccountResponseDto;
import com.java.admin.entity.user.UserEntity;
import com.java.admin.security.AuthProvider;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // Example method to map UserAccountDto to UserAccount entity
    public UserEntity toEntity(CreateUserRequestDto createUserRequestDto, AuthProvider authProvider) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(createUserRequestDto.username());
        userEntity.setPassword(createUserRequestDto.password());
        userEntity.setEmail(createUserRequestDto.email());
        userEntity.setFirstName(createUserRequestDto.firstName());
        userEntity.setLastName(createUserRequestDto.lastName());
        userEntity.setPhoneNumber(createUserRequestDto.phoneNumber());
        userEntity.setAddress(createUserRequestDto.address());
        userEntity.setProvider(authProvider);
        return userEntity;
    }

    // Example method to map UserAccount entity to UserAccountDto
    public UserAccountResponseDto toDto(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return new UserAccountResponseDto(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPhoneNumber(),
                entity.getAddress()
        );
    }
}
