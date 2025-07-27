package com.java.admin.unit.security;

import com.java.admin.security.CustomAuthUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomAuthUserTest {

    private static final Long TEST_ID = 1L;
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "testpassword";
    private static final boolean ENABLED = true;
    private static final boolean ACCOUNT_NON_EXPIRED = true;
    private static final boolean CREDENTIALS_NON_EXPIRED = true;
    private static final boolean ACCOUNT_NON_LOCKED = true;

    private Collection<GrantedAuthority> authorities;
    private CustomAuthUser customAuthUser;

    @BeforeEach
    void setUp() {
        authorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );

        customAuthUser = new CustomAuthUser(
                TEST_ID,
                TEST_USERNAME,
                TEST_PASSWORD,
                ENABLED,
                ACCOUNT_NON_EXPIRED,
                CREDENTIALS_NON_EXPIRED,
                ACCOUNT_NON_LOCKED,
                authorities
        );
    }

    @Test
    @DisplayName("Should create CustomAuthUser with valid parameters")
    void constructor_shouldCreateCustomAuthUser_whenValidParameters() {
        // Act & Assert
        assertNotNull(customAuthUser);
        assertEquals(TEST_ID, customAuthUser.getId());
        assertEquals(TEST_USERNAME, customAuthUser.getUsername());
        assertEquals(TEST_PASSWORD, customAuthUser.getPassword());
        assertTrue(customAuthUser.isEnabled());
        assertTrue(customAuthUser.isAccountNonExpired());
        assertTrue(customAuthUser.isCredentialsNonExpired());
        assertTrue(customAuthUser.isAccountNonLocked());
        assertEquals(2, customAuthUser.getAuthorities().size());
    }

    @Test
    @DisplayName("Should throw exception when username is null")
    void constructor_shouldThrowException_whenUsernameIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new CustomAuthUser(
                        TEST_ID,
                        null,
                        TEST_PASSWORD,
                        ENABLED,
                        ACCOUNT_NON_EXPIRED,
                        CREDENTIALS_NON_EXPIRED,
                        ACCOUNT_NON_LOCKED,
                        authorities
                )
        );

        assertEquals("Cannot pass null or empty values to constructor", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when username is empty")
    void constructor_shouldThrowException_whenUsernameIsEmpty() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new CustomAuthUser(
                        TEST_ID,
                        "",
                        TEST_PASSWORD,
                        ENABLED,
                        ACCOUNT_NON_EXPIRED,
                        CREDENTIALS_NON_EXPIRED,
                        ACCOUNT_NON_LOCKED,
                        authorities
                )
        );

        assertEquals("Cannot pass null or empty values to constructor", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when password is null")
    void constructor_shouldThrowException_whenPasswordIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new CustomAuthUser(
                        TEST_ID,
                        TEST_USERNAME,
                        null,
                        ENABLED,
                        ACCOUNT_NON_EXPIRED,
                        CREDENTIALS_NON_EXPIRED,
                        ACCOUNT_NON_LOCKED,
                        authorities
                )
        );

        assertEquals("Cannot pass null or empty values to constructor", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when authorities collection is null")
    void constructor_shouldThrowException_whenAuthoritiesIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new CustomAuthUser(
                        TEST_ID,
                        TEST_USERNAME,
                        TEST_PASSWORD,
                        ENABLED,
                        ACCOUNT_NON_EXPIRED,
                        CREDENTIALS_NON_EXPIRED,
                        ACCOUNT_NON_LOCKED,
                        null
                )
        );

        assertEquals("Cannot pass a null GrantedAuthority collection", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when authorities collection contains null element")
    void constructor_shouldThrowException_whenAuthoritiesContainsNullElement() {
        // Arrange
        Collection<GrantedAuthority> authoritiesWithNull = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER"),
                null
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new CustomAuthUser(
                        TEST_ID,
                        TEST_USERNAME,
                        TEST_PASSWORD,
                        ENABLED,
                        ACCOUNT_NON_EXPIRED,
                        CREDENTIALS_NON_EXPIRED,
                        ACCOUNT_NON_LOCKED,
                        authoritiesWithNull
                )
        );

        assertEquals("GrantedAuthority list cannot contain any null elements", exception.getMessage());
    }

    @Test
    @DisplayName("Should return correct ID")
    void getId_shouldReturnCorrectId() {
        // Act
        Long id = customAuthUser.getId();

        // Assert
        assertEquals(TEST_ID, id);
    }

    @Test
    @DisplayName("Should return correct username")
    void getUsername_shouldReturnCorrectUsername() {
        // Act
        String username = customAuthUser.getUsername();

        // Assert
        assertEquals(TEST_USERNAME, username);
    }

    @Test
    @DisplayName("Should return correct password")
    void getPassword_shouldReturnCorrectPassword() {
        // Act
        String password = customAuthUser.getPassword();

        // Assert
        assertEquals(TEST_PASSWORD, password);
    }

    @Test
    @DisplayName("Should return correct enabled status")
    void isEnabled_shouldReturnCorrectStatus() {
        // Act
        boolean enabled = customAuthUser.isEnabled();

        // Assert
        assertTrue(enabled);
    }

    @Test
    @DisplayName("Should return correct account non expired status")
    void isAccountNonExpired_shouldReturnCorrectStatus() {
        // Act
        boolean accountNonExpired = customAuthUser.isAccountNonExpired();

        // Assert
        assertTrue(accountNonExpired);
    }

    @Test
    @DisplayName("Should return correct credentials non expired status")
    void isCredentialsNonExpired_shouldReturnCorrectStatus() {
        // Act
        boolean credentialsNonExpired = customAuthUser.isCredentialsNonExpired();

        // Assert
        assertTrue(credentialsNonExpired);
    }

    @Test
    @DisplayName("Should return correct account non locked status")
    void isAccountNonLocked_shouldReturnCorrectStatus() {
        // Act
        boolean accountNonLocked = customAuthUser.isAccountNonLocked();

        // Assert
        assertTrue(accountNonLocked);
    }

    @Test
    @DisplayName("Should return sorted authorities")
    void getAuthorities_shouldReturnSortedAuthorities() {
        // Arrange
        Collection<GrantedAuthority> unsortedAuthorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );

        CustomAuthUser user = new CustomAuthUser(
                TEST_ID,
                TEST_USERNAME,
                TEST_PASSWORD,
                ENABLED,
                ACCOUNT_NON_EXPIRED,
                CREDENTIALS_NON_EXPIRED,
                ACCOUNT_NON_LOCKED,
                unsortedAuthorities
        );

        // Act
        Collection<GrantedAuthority> sortedAuthorities = user.getAuthorities();

        // Assert
        assertNotNull(sortedAuthorities);
        assertEquals(2, sortedAuthorities.size());

        // Verificar que están ordenadas alfabéticamente
        GrantedAuthority[] authArray = sortedAuthorities.toArray(new GrantedAuthority[0]);
        assertEquals("ROLE_ADMIN", authArray[0].getAuthority());
        assertEquals("ROLE_USER", authArray[1].getAuthority());
    }

    @Test
    @DisplayName("Should erase credentials when eraseCredentials is called")
    void eraseCredentials_shouldErasePassword() {
        // Arrange
        assertNotNull(customAuthUser.getPassword());
        assertEquals(TEST_PASSWORD, customAuthUser.getPassword());

        // Act
        customAuthUser.eraseCredentials();

        // Assert
        assertNull(customAuthUser.getPassword());
    }

    @Test
    @DisplayName("Should create user with disabled status")
    void constructor_shouldCreateDisabledUser_whenEnabledIsFalse() {
        // Arrange & Act
        CustomAuthUser disabledUser = new CustomAuthUser(
                TEST_ID,
                TEST_USERNAME,
                TEST_PASSWORD,
                false,
                ACCOUNT_NON_EXPIRED,
                CREDENTIALS_NON_EXPIRED,
                ACCOUNT_NON_LOCKED,
                authorities
        );

        // Assert
        assertFalse(disabledUser.isEnabled());
        assertTrue(disabledUser.isAccountNonExpired());
        assertTrue(disabledUser.isCredentialsNonExpired());
        assertTrue(disabledUser.isAccountNonLocked());
    }

    @Test
    @DisplayName("Should create user with expired account")
    void constructor_shouldCreateUserWithExpiredAccount_whenAccountNonExpiredIsFalse() {
        // Arrange & Act
        CustomAuthUser expiredUser = new CustomAuthUser(
                TEST_ID,
                TEST_USERNAME,
                TEST_PASSWORD,
                ENABLED,
                false,
                CREDENTIALS_NON_EXPIRED,
                ACCOUNT_NON_LOCKED,
                authorities
        );

        // Assert
        assertTrue(expiredUser.isEnabled());
        assertFalse(expiredUser.isAccountNonExpired());
        assertTrue(expiredUser.isCredentialsNonExpired());
        assertTrue(expiredUser.isAccountNonLocked());
    }

    @Test
    @DisplayName("Should create user with expired credentials")
    void constructor_shouldCreateUserWithExpiredCredentials_whenCredentialsNonExpiredIsFalse() {
        // Arrange & Act
        CustomAuthUser expiredCredentialsUser = new CustomAuthUser(
                TEST_ID,
                TEST_USERNAME,
                TEST_PASSWORD,
                ENABLED,
                ACCOUNT_NON_EXPIRED,
                false,
                ACCOUNT_NON_LOCKED,
                authorities
        );

        // Assert
        assertTrue(expiredCredentialsUser.isEnabled());
        assertTrue(expiredCredentialsUser.isAccountNonExpired());
        assertFalse(expiredCredentialsUser.isCredentialsNonExpired());
        assertTrue(expiredCredentialsUser.isAccountNonLocked());
    }

    @Test
    @DisplayName("Should create user with locked account")
    void constructor_shouldCreateUserWithLockedAccount_whenAccountNonLockedIsFalse() {
        // Arrange & Act
        CustomAuthUser lockedUser = new CustomAuthUser(
                TEST_ID,
                TEST_USERNAME,
                TEST_PASSWORD,
                ENABLED,
                ACCOUNT_NON_EXPIRED,
                CREDENTIALS_NON_EXPIRED,
                false,
                authorities
        );

        // Assert
        assertTrue(lockedUser.isEnabled());
        assertTrue(lockedUser.isAccountNonExpired());
        assertTrue(lockedUser.isCredentialsNonExpired());
        assertFalse(lockedUser.isAccountNonLocked());
    }

    @Test
    @DisplayName("Should handle empty authorities collection")
    void constructor_shouldHandleEmptyAuthorities() {
        // Arrange & Act
        CustomAuthUser userWithNoAuthorities = new CustomAuthUser(
                TEST_ID,
                TEST_USERNAME,
                TEST_PASSWORD,
                ENABLED,
                ACCOUNT_NON_EXPIRED,
                CREDENTIALS_NON_EXPIRED,
                ACCOUNT_NON_LOCKED,
                List.of()
        );

        // Assert
        assertNotNull(userWithNoAuthorities.getAuthorities());
        assertTrue(userWithNoAuthorities.getAuthorities().isEmpty());
    }

    @Test
    @DisplayName("Should return immutable authorities collection")
    void getAuthorities_shouldReturnImmutableCollection() {
        // Act
        Collection<GrantedAuthority> authorities = customAuthUser.getAuthorities();

        // Assert
        assertThrows(UnsupportedOperationException.class, () ->
                authorities.add(new SimpleGrantedAuthority("ROLE_TEST"))
        );
    }

    @Test
    @DisplayName("Should sort authorities with null authority last")
    void getAuthorities_shouldSortAuthoritiesWithNullLast() {
        // Arrange
        GrantedAuthority customAuthority = new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return null;
            }
        };

        Collection<GrantedAuthority> authoritiesWithNull = List.of(
                new SimpleGrantedAuthority("ROLE_USER"),
                customAuthority,
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );

        CustomAuthUser user = new CustomAuthUser(
                TEST_ID,
                TEST_USERNAME,
                TEST_PASSWORD,
                ENABLED,
                ACCOUNT_NON_EXPIRED,
                CREDENTIALS_NON_EXPIRED,
                ACCOUNT_NON_LOCKED,
                authoritiesWithNull
        );

        // Act
        Collection<GrantedAuthority> sortedAuthorities = user.getAuthorities();

        // Assert
        GrantedAuthority[] authArray = sortedAuthorities.toArray(new GrantedAuthority[0]);
        assertEquals(3, authArray.length);
        assertEquals("ROLE_ADMIN", authArray[0].getAuthority());
        assertEquals("ROLE_USER", authArray[1].getAuthority());
        assertNull(authArray[2].getAuthority()); // Null authority should be last
    }

    @Test
    @DisplayName("Should sort authorities alphabetically when no null authorities")
    void getAuthorities_shouldSortAuthoritiesAlphabetically() {
        // Arrange
        Collection<GrantedAuthority> unsortedAuthorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_MANAGER"),
                new SimpleGrantedAuthority("ROLE_DEVELOPER")
        );

        CustomAuthUser user = new CustomAuthUser(
                TEST_ID,
                TEST_USERNAME,
                TEST_PASSWORD,
                ENABLED,
                ACCOUNT_NON_EXPIRED,
                CREDENTIALS_NON_EXPIRED,
                ACCOUNT_NON_LOCKED,
                unsortedAuthorities
        );

        // Act
        Collection<GrantedAuthority> sortedAuthorities = user.getAuthorities();

        // Assert
        GrantedAuthority[] authArray = sortedAuthorities.toArray(new GrantedAuthority[0]);
        assertEquals("ROLE_ADMIN", authArray[0].getAuthority());
        assertEquals("ROLE_DEVELOPER", authArray[1].getAuthority());
        assertEquals("ROLE_MANAGER", authArray[2].getAuthority());
        assertEquals("ROLE_USER", authArray[3].getAuthority());
    }

    @Test
    @DisplayName("Should handle multiple null authorities correctly")
    void getAuthorities_shouldHandleMultipleNullAuthorities() {
        // Arrange
        GrantedAuthority customAuthority1 = new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return null;
            }
        };

        GrantedAuthority customAuthority2 = new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return null;
            }
        };

        Collection<GrantedAuthority> authoritiesWithMultipleNull = List.of(
                new SimpleGrantedAuthority("ROLE_USER"),
                customAuthority1,
                customAuthority2,
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );

        CustomAuthUser user = new CustomAuthUser(
                TEST_ID,
                TEST_USERNAME,
                TEST_PASSWORD,
                ENABLED,
                ACCOUNT_NON_EXPIRED,
                CREDENTIALS_NON_EXPIRED,
                ACCOUNT_NON_LOCKED,
                authoritiesWithMultipleNull
        );

        // Act
        Collection<GrantedAuthority> sortedAuthorities = user.getAuthorities();

        // Assert
        GrantedAuthority[] authArray = sortedAuthorities.toArray(new GrantedAuthority[0]);
        assertEquals(4, authArray.length);
        assertEquals("ROLE_ADMIN", authArray[0].getAuthority());
        assertEquals("ROLE_USER", authArray[1].getAuthority());
        assertNull(authArray[2].getAuthority()); // First null authority
        assertNull(authArray[3].getAuthority()); // Second null authority
    }

    @Test
    @DisplayName("Should maintain consistent sorting order")
    void getAuthorities_shouldMaintainConsistentSortingOrder() {
        // Arrange
        Collection<GrantedAuthority> authorities1 = List.of(
                new SimpleGrantedAuthority("ROLE_Z"),
                new SimpleGrantedAuthority("ROLE_A"),
                new SimpleGrantedAuthority("ROLE_M")
        );

        Collection<GrantedAuthority> authorities2 = List.of(
                new SimpleGrantedAuthority("ROLE_A"),
                new SimpleGrantedAuthority("ROLE_Z"),
                new SimpleGrantedAuthority("ROLE_M")
        );

        CustomAuthUser user1 = new CustomAuthUser(
                TEST_ID,
                TEST_USERNAME,
                TEST_PASSWORD,
                ENABLED,
                ACCOUNT_NON_EXPIRED,
                CREDENTIALS_NON_EXPIRED,
                ACCOUNT_NON_LOCKED,
                authorities1
        );

        CustomAuthUser user2 = new CustomAuthUser(
                TEST_ID,
                TEST_USERNAME,
                TEST_PASSWORD,
                ENABLED,
                ACCOUNT_NON_EXPIRED,
                CREDENTIALS_NON_EXPIRED,
                ACCOUNT_NON_LOCKED,
                authorities2
        );

        // Act
        GrantedAuthority[] sortedAuthorities1 = user1.getAuthorities().toArray(new GrantedAuthority[0]);
        GrantedAuthority[] sortedAuthorities2 = user2.getAuthorities().toArray(new GrantedAuthority[0]);

        // Assert
        assertEquals(sortedAuthorities1.length, sortedAuthorities2.length);
        for (int i = 0; i < sortedAuthorities1.length; i++) {
            assertEquals(sortedAuthorities1[i].getAuthority(), sortedAuthorities2[i].getAuthority());
        }
    }

    @Test
    @DisplayName("Should handle case sensitive authority names correctly")
    void getAuthorities_shouldHandleCaseSensitiveAuthorities() {
        // Arrange
        Collection<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("role_user"),
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("Role_Admin"),
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );

        CustomAuthUser user = new CustomAuthUser(
                TEST_ID,
                TEST_USERNAME,
                TEST_PASSWORD,
                ENABLED,
                ACCOUNT_NON_EXPIRED,
                CREDENTIALS_NON_EXPIRED,
                ACCOUNT_NON_LOCKED,
                authorities
        );

        // Act
        Collection<GrantedAuthority> sortedAuthorities = user.getAuthorities();

        // Assert
        GrantedAuthority[] authArray = sortedAuthorities.toArray(new GrantedAuthority[0]);
        assertEquals(4, authArray.length);
        assertEquals("ROLE_ADMIN", authArray[0].getAuthority());
        assertEquals("ROLE_USER", authArray[1].getAuthority());
        assertEquals("Role_Admin", authArray[2].getAuthority());
        assertEquals("role_user", authArray[3].getAuthority());
    }
}