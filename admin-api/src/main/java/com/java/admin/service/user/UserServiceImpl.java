package com.java.admin.service.user;

import com.java.admin.dto.user.request.UpdateUserRequestDto;
import com.java.admin.dto.user.response.GetUserResponseDto;
import com.java.admin.dto.user.response.UpdateUserResponseDto;
import com.java.admin.entity.user.UserEntity;
import com.java.admin.exception.user.UserException;
import com.java.admin.mapper.user.UserMapper;
import com.java.admin.repository.user.UserRepository;
import com.java.admin.usecase.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public GetUserResponseDto getUserById(Long userId) {

        UserEntity userEntity = userRepository.findUserEntityById(userId).orElse(null);
        if (userEntity == null) {
            throw new UserException(
                    UserException.USER_ACCOUNT_NOT_FOUND_CODE,
                    UserException.USER_ACCOUNT_NOT_FOUND_MESSAGE,
                    UserException.USER_ACCOUNT_NOT_FOUND_CAUSE
            );
        }

        return userMapper.toGetUserResponseDto(userEntity);
    }

    @Override
    public UpdateUserResponseDto updateUser(Long userId, UpdateUserRequestDto updateUserRequestDto) {

        UserEntity userEntity = userRepository.findUserEntityById(userId).orElse(null);
        if (userEntity == null) {
            throw new UserException(
                    UserException.USER_ACCOUNT_NOT_FOUND_CODE,
                    UserException.USER_ACCOUNT_NOT_FOUND_MESSAGE,
                    UserException.USER_ACCOUNT_NOT_FOUND_CAUSE
            );
        }

        userEntity.setUsername(updateUserRequestDto.username());
        userEntity.setFirstName(updateUserRequestDto.firstName());
        userEntity.setLastName(updateUserRequestDto.lastName());
        userEntity.setPhoneNumber(updateUserRequestDto.phoneNumber());
        userEntity.setAddress(updateUserRequestDto.address());
        userEntity = userRepository.save(userEntity);

        return new UpdateUserResponseDto(
                userEntity.getUsername(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getPhoneNumber(),
                userEntity.getAddress()
        );
    }
}