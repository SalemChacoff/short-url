package com.java.admin.unit.mapper.account;

import com.java.admin.dto.account.request.CreateAccountRequestDto;
import com.java.admin.entity.user.UserEntity;
import com.java.admin.mapper.account.AccountMapper;
import com.java.admin.security.AuthProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccountMapper Tests")
class AccountMapperTest {

    private static final String VALID_USERNAME = "johndoe232";
    private static final String VALID_PASSWORD = "Password123!";
    private static final String VALID_EMAIL = "john.doe@example.com";
    private static final String VALID_FIRST_NAME = "John";
    private static final String VALID_LAST_NAME = "Doe";
    private static final String VALID_PHONE_NUMBER = "01234567890";
    private static final String VALID_ADDRESS = "123 Example Street";

    @InjectMocks
    private AccountMapper accountMapper;

    private CreateAccountRequestDto createAccountRequestDto;

    @BeforeEach
    void setUp() {
        createAccountRequestDto = new CreateAccountRequestDto(
                VALID_USERNAME,
                VALID_PASSWORD,
                VALID_EMAIL,
                VALID_FIRST_NAME,
                VALID_LAST_NAME,
                VALID_PHONE_NUMBER,
                VALID_ADDRESS
        );
    }

    @Test
    @DisplayName("Should map CreateAccountRequestDto to UserEntity with LOCAL provider")
    void toEntity_shouldMapCorrectly_whenValidRequestAndLocalProvider() {
        // Arrange
        AuthProvider authProvider = AuthProvider.LOCAL;

        // Act
        UserEntity result = accountMapper.toEntity(createAccountRequestDto, authProvider);

        // Assert
        assertNotNull(result);
        assertEquals(VALID_USERNAME, result.getUsername());
        assertEquals(VALID_PASSWORD, result.getPassword());
        assertEquals(VALID_EMAIL, result.getEmail());
        assertEquals(VALID_FIRST_NAME, result.getFirstName());
        assertEquals(VALID_LAST_NAME, result.getLastName());
        assertEquals(VALID_PHONE_NUMBER, result.getPhoneNumber());
        assertEquals(VALID_ADDRESS, result.getAddress());
        assertEquals(AuthProvider.LOCAL, result.getProvider());
    }

    @Test
    @DisplayName("Should map CreateAccountRequestDto to UserEntity with GOOGLE provider")
    void toEntity_shouldMapCorrectly_whenValidRequestAndGoogleProvider() {
        // Arrange
        AuthProvider authProvider = AuthProvider.GOOGLE;

        // Act
        UserEntity result = accountMapper.toEntity(createAccountRequestDto, authProvider);

        // Assert
        assertNotNull(result);
        assertEquals(VALID_USERNAME, result.getUsername());
        assertEquals(VALID_PASSWORD, result.getPassword());
        assertEquals(VALID_EMAIL, result.getEmail());
        assertEquals(VALID_FIRST_NAME, result.getFirstName());
        assertEquals(VALID_LAST_NAME, result.getLastName());
        assertEquals(VALID_PHONE_NUMBER, result.getPhoneNumber());
        assertEquals(VALID_ADDRESS, result.getAddress());
        assertEquals(AuthProvider.GOOGLE, result.getProvider());
    }

    @Test
    @DisplayName("Should handle null values correctly in request DTO")
    void toEntity_shouldHandleNullValues_whenRequestHasNullFields() {
        // Arrange
        CreateAccountRequestDto requestWithNulls = new CreateAccountRequestDto(
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        AuthProvider authProvider = AuthProvider.LOCAL;

        // Act
        UserEntity result = accountMapper.toEntity(requestWithNulls, authProvider);

        // Assert
        assertNotNull(result);
        assertNull(result.getUsername());
        assertNull(result.getPassword());
        assertNull(result.getEmail());
        assertNull(result.getFirstName());
        assertNull(result.getLastName());
        assertNull(result.getPhoneNumber());
        assertNull(result.getAddress());
        assertEquals(AuthProvider.LOCAL, result.getProvider());
    }

    @Test
    @DisplayName("Should handle empty strings correctly in request DTO")
    void toEntity_shouldHandleEmptyStrings_whenRequestHasEmptyFields() {
        // Arrange
        CreateAccountRequestDto requestWithEmptyStrings = new CreateAccountRequestDto(
                "",
                "",
                "",
                "",
                "",
                "",
                ""
        );
        AuthProvider authProvider = AuthProvider.LOCAL;

        // Act
        UserEntity result = accountMapper.toEntity(requestWithEmptyStrings, authProvider);

        // Assert
        assertNotNull(result);
        assertEquals("", result.getUsername());
        assertEquals("", result.getPassword());
        assertEquals("", result.getEmail());
        assertEquals("", result.getFirstName());
        assertEquals("", result.getLastName());
        assertEquals("", result.getPhoneNumber());
        assertEquals("", result.getAddress());
        assertEquals(AuthProvider.LOCAL, result.getProvider());
    }

    @Test
    @DisplayName("Should throw NullPointerException when request DTO is null")
    void toEntity_shouldThrowNullPointerException_whenRequestDtoIsNull() {
        // Arrange
        AuthProvider authProvider = AuthProvider.LOCAL;

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                accountMapper.toEntity(null, authProvider)
        );
    }

    @Test
    @DisplayName("Should map correctly when auth provider is null")
    void toEntity_shouldMapCorrectly_whenAuthProviderIsNull() {
        // Act
        UserEntity result = accountMapper.toEntity(createAccountRequestDto, null);

        // Assert
        assertNotNull(result);
        assertEquals(VALID_USERNAME, result.getUsername());
        assertEquals(VALID_PASSWORD, result.getPassword());
        assertEquals(VALID_EMAIL, result.getEmail());
        assertEquals(VALID_FIRST_NAME, result.getFirstName());
        assertEquals(VALID_LAST_NAME, result.getLastName());
        assertEquals(VALID_PHONE_NUMBER, result.getPhoneNumber());
        assertEquals(VALID_ADDRESS, result.getAddress());
        assertNull(result.getProvider());
    }
}