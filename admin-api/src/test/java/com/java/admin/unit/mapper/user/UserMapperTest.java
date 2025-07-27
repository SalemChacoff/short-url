package com.java.admin.unit.mapper.user;

import com.java.admin.dto.user.response.GetUserResponseDto;
import com.java.admin.entity.user.UserEntity;
import com.java.admin.mapper.user.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserMapper Tests")
class UserMapperTest {

    private static final String VALID_USERNAME = "johndoe232";
    private static final String VALID_EMAIL = "john.doe@example.com";
    private static final String VALID_FIRST_NAME = "John";
    private static final String VALID_LAST_NAME = "Doe";
    private static final String VALID_PHONE_NUMBER = "01234567890";
    private static final String VALID_ADDRESS = "123 Example Street";

    @InjectMocks
    private UserMapper userMapper;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setUsername(VALID_USERNAME);
        userEntity.setEmail(VALID_EMAIL);
        userEntity.setFirstName(VALID_FIRST_NAME);
        userEntity.setLastName(VALID_LAST_NAME);
        userEntity.setPhoneNumber(VALID_PHONE_NUMBER);
        userEntity.setAddress(VALID_ADDRESS);
    }

    @Test
    @DisplayName("Should map UserEntity to GetUserResponseDto correctly")
    void toGetUserResponseDto_shouldMapCorrectly_whenValidUserEntity() {
        // Act
        GetUserResponseDto result = userMapper.toGetUserResponseDto(userEntity);

        // Assert
        assertNotNull(result);
        assertEquals(VALID_USERNAME, result.username());
        assertEquals(VALID_EMAIL, result.email());
        assertEquals(VALID_FIRST_NAME, result.firstName());
        assertEquals(VALID_LAST_NAME, result.lastName());
        assertEquals(VALID_PHONE_NUMBER, result.phoneNumber());
        assertEquals(VALID_ADDRESS, result.address());
    }

    @Test
    @DisplayName("Should handle UserEntity with null fields correctly")
    void toGetUserResponseDto_shouldHandleNullFields_whenUserEntityHasNullValues() {
        // Arrange
        UserEntity entityWithNulls = new UserEntity();
        entityWithNulls.setUsername(VALID_USERNAME);
        entityWithNulls.setEmail(VALID_EMAIL);

        // Act
        GetUserResponseDto result = userMapper.toGetUserResponseDto(entityWithNulls);

        // Assert
        assertNotNull(result);
        assertEquals(VALID_USERNAME, result.username());
        assertEquals(VALID_EMAIL, result.email());
        assertNull(result.firstName());
        assertNull(result.lastName());
        assertNull(result.phoneNumber());
        assertNull(result.address());
    }

    @Test
    @DisplayName("Should handle UserEntity with empty strings correctly")
    void toGetUserResponseDto_shouldHandleEmptyStrings_whenUserEntityHasEmptyFields() {
        // Arrange
        UserEntity entityWithEmptyStrings = new UserEntity();
        entityWithEmptyStrings.setUsername("");
        entityWithEmptyStrings.setEmail("");
        entityWithEmptyStrings.setFirstName("");
        entityWithEmptyStrings.setLastName("");
        entityWithEmptyStrings.setPhoneNumber("");
        entityWithEmptyStrings.setAddress("");

        // Act
        GetUserResponseDto result = userMapper.toGetUserResponseDto(entityWithEmptyStrings);

        // Assert
        assertNotNull(result);
        assertEquals("", result.username());
        assertEquals("", result.email());
        assertEquals("", result.firstName());
        assertEquals("", result.lastName());
        assertEquals("", result.phoneNumber());
        assertEquals("", result.address());
    }

    @Test
    @DisplayName("Should throw NullPointerException when UserEntity is null")
    void toGetUserResponseDto_shouldThrowNullPointerException_whenUserEntityIsNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                userMapper.toGetUserResponseDto(null)
        );
    }

    @Test
    @DisplayName("Should handle UserEntity with only required fields")
    void toGetUserResponseDto_shouldMapCorrectly_whenUserEntityHasOnlyRequiredFields() {
        // Arrange
        UserEntity minimalEntity = new UserEntity();
        minimalEntity.setUsername(VALID_USERNAME);
        minimalEntity.setEmail(VALID_EMAIL);

        // Act
        GetUserResponseDto result = userMapper.toGetUserResponseDto(minimalEntity);

        // Assert
        assertNotNull(result);
        assertEquals(VALID_USERNAME, result.username());
        assertEquals(VALID_EMAIL, result.email());
        assertNull(result.firstName());
        assertNull(result.lastName());
        assertNull(result.phoneNumber());
        assertNull(result.address());
    }
}