package com.java.admin.unit.service.user;

import com.java.admin.dto.user.request.UpdateUserRequestDto;
import com.java.admin.dto.user.response.GetUserResponseDto;
import com.java.admin.dto.user.response.UpdateUserResponseDto;
import com.java.admin.entity.user.UserEntity;
import com.java.admin.exception.user.UserException;
import com.java.admin.mapper.user.UserMapper;
import com.java.admin.repository.user.UserRepository;
import com.java.admin.service.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity userEntity;
    private UpdateUserRequestDto updateUserRequest;
    private GetUserResponseDto getUserResponse;

    private static final Long VALID_USER_ID = 1L;
    private static final Long INVALID_USER_ID = 999L;
    private static final String VALID_USERNAME = "testuser";
    private static final String VALID_EMAIL = "test@example.com";
    private static final String VALID_FIRST_NAME = "John";
    private static final String VALID_LAST_NAME = "Doe";
    private static final String VALID_PHONE_NUMBER = "+1234567890";
    private static final String VALID_ADDRESS = "123 Main St";

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(VALID_USER_ID);
        userEntity.setUsername(VALID_USERNAME);
        userEntity.setEmail(VALID_EMAIL);
        userEntity.setFirstName(VALID_FIRST_NAME);
        userEntity.setLastName(VALID_LAST_NAME);
        userEntity.setPhoneNumber(VALID_PHONE_NUMBER);
        userEntity.setAddress(VALID_ADDRESS);
        userEntity.setEnabled(true);
        userEntity.setAccountNonExpired(true);
        userEntity.setCredentialsNonExpired(true);
        userEntity.setAccountNonLocked(true);
        userEntity.setCreatedAt(OffsetDateTime.now());
        userEntity.setUpdatedAt(OffsetDateTime.now());

        updateUserRequest = new UpdateUserRequestDto(
                VALID_USERNAME,
                VALID_FIRST_NAME,
                VALID_LAST_NAME,
                VALID_PHONE_NUMBER,
                VALID_ADDRESS
        );

        getUserResponse = new GetUserResponseDto(
                VALID_USERNAME,
                VALID_EMAIL,
                VALID_FIRST_NAME,
                VALID_LAST_NAME,
                VALID_PHONE_NUMBER,
                VALID_ADDRESS
        );
    }

    @Test
    @DisplayName("Should return user details when user exists")
    void getUserById_shouldReturnUserDetails_whenUserExists() {
        // Arrange
        when(userRepository.findUserEntityById(VALID_USER_ID)).thenReturn(Optional.of(userEntity));
        when(userMapper.toGetUserResponseDto(userEntity)).thenReturn(getUserResponse);

        // Act
        GetUserResponseDto result = userService.getUserById(VALID_USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(VALID_USERNAME, result.username());
        assertEquals(VALID_EMAIL, result.email());
        assertEquals(VALID_FIRST_NAME, result.firstName());
        assertEquals(VALID_LAST_NAME, result.lastName());
        assertEquals(VALID_PHONE_NUMBER, result.phoneNumber());
        assertEquals(VALID_ADDRESS, result.address());

        verify(userRepository).findUserEntityById(VALID_USER_ID);
        verify(userMapper).toGetUserResponseDto(userEntity);
    }

    @Test
    @DisplayName("Should throw UserException when user not found")
    void getUserById_shouldThrowUserException_whenUserNotFound() {
        // Arrange
        when(userRepository.findUserEntityById(INVALID_USER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        UserException exception = assertThrows(
                UserException.class,
                () -> userService.getUserById(INVALID_USER_ID)
        );

        assertEquals(UserException.USER_ACCOUNT_NOT_FOUND_CODE, exception.getErrorCode());
        assertEquals(UserException.USER_ACCOUNT_NOT_FOUND_MESSAGE, exception.getErrorMessage());
        assertEquals(UserException.USER_ACCOUNT_NOT_FOUND_CAUSE, exception.getErrorCause());

        verify(userRepository).findUserEntityById(INVALID_USER_ID);
        verify(userMapper, never()).toGetUserResponseDto(any());
    }

    @Test
    @DisplayName("Should update user successfully when user exists")
    void updateUser_shouldUpdateUserSuccessfully_whenUserExists() {
        // Arrange
        when(userRepository.findUserEntityById(VALID_USER_ID)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        // Act
        UpdateUserResponseDto result = userService.updateUser(VALID_USER_ID, updateUserRequest);

        // Assert
        assertNotNull(result);
        assertEquals(VALID_USERNAME, result.username());
        assertEquals(VALID_FIRST_NAME, result.firstName());
        assertEquals(VALID_LAST_NAME, result.lastName());
        assertEquals(VALID_PHONE_NUMBER, result.phoneNumber());
        assertEquals(VALID_ADDRESS, result.address());

        verify(userRepository).findUserEntityById(VALID_USER_ID);
        verify(userRepository).save(userEntity);

        // Verificar que los campos se actualizaron en la entidad
        assertEquals(VALID_USERNAME, userEntity.getUsername());
        assertEquals(VALID_FIRST_NAME, userEntity.getFirstName());
        assertEquals(VALID_LAST_NAME, userEntity.getLastName());
        assertEquals(VALID_PHONE_NUMBER, userEntity.getPhoneNumber());
        assertEquals(VALID_ADDRESS, userEntity.getAddress());
    }

    @Test
    @DisplayName("Should throw UserException when updating non-existent user")
    void updateUser_shouldThrowUserException_whenUserNotFound() {
        // Arrange
        when(userRepository.findUserEntityById(INVALID_USER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        UserException exception = assertThrows(
                UserException.class,
                () -> userService.updateUser(INVALID_USER_ID, updateUserRequest)
        );

        assertEquals(UserException.USER_ACCOUNT_NOT_FOUND_CODE, exception.getErrorCode());
        assertEquals(UserException.USER_ACCOUNT_NOT_FOUND_MESSAGE, exception.getErrorMessage());
        assertEquals(UserException.USER_ACCOUNT_NOT_FOUND_CAUSE, exception.getErrorCause());

        verify(userRepository).findUserEntityById(INVALID_USER_ID);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should handle null fields in update request")
    void updateUser_shouldHandleNullFields_whenUpdateRequestHasNullValues() {
        // Arrange
        UpdateUserRequestDto requestWithNulls = new UpdateUserRequestDto(
                null,
                null,
                null,
                null,
                null
        );

        when(userRepository.findUserEntityById(VALID_USER_ID)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        // Act
        UpdateUserResponseDto result = userService.updateUser(VALID_USER_ID, requestWithNulls);

        // Assert
        assertNotNull(result);
        verify(userRepository).findUserEntityById(VALID_USER_ID);
        verify(userRepository).save(userEntity);
    }

    @Test
    @DisplayName("Should verify repository interactions for getUserById")
    void getUserById_shouldVerifyRepositoryInteractions_whenCalled() {
        // Arrange
        when(userRepository.findUserEntityById(VALID_USER_ID)).thenReturn(Optional.of(userEntity));
        when(userMapper.toGetUserResponseDto(userEntity)).thenReturn(getUserResponse);

        // Act
        userService.getUserById(VALID_USER_ID);

        // Assert
        verify(userRepository, times(1)).findUserEntityById(VALID_USER_ID);
        verify(userMapper, times(1)).toGetUserResponseDto(userEntity);
        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    @DisplayName("Should verify repository interactions for updateUser")
    void updateUser_shouldVerifyRepositoryInteractions_whenCalled() {
        // Arrange
        when(userRepository.findUserEntityById(VALID_USER_ID)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        // Act
        userService.updateUser(VALID_USER_ID, updateUserRequest);

        // Assert
        verify(userRepository, times(1)).findUserEntityById(VALID_USER_ID);
        verify(userRepository, times(1)).save(userEntity);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userMapper);
    }
}