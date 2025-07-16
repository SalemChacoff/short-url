package com.java.admin.mapper.account;

import com.java.admin.dto.account.request.CreateAccountRequestDto;
import com.java.admin.entity.user.UserEntity;
import com.java.admin.security.AuthProvider;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    // Example method to map UserAccountDto to UserAccount entity
    public UserEntity toEntity(CreateAccountRequestDto createAccountRequestDto, AuthProvider authProvider) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(createAccountRequestDto.username());
        userEntity.setPassword(createAccountRequestDto.password());
        userEntity.setEmail(createAccountRequestDto.email());
        userEntity.setFirstName(createAccountRequestDto.firstName());
        userEntity.setLastName(createAccountRequestDto.lastName());
        userEntity.setPhoneNumber(createAccountRequestDto.phoneNumber());
        userEntity.setAddress(createAccountRequestDto.address());
        userEntity.setProvider(authProvider);
        return userEntity;
    }
}
