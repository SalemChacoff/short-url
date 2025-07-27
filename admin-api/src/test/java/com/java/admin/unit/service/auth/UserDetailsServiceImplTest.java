package com.java.admin.unit.service.auth;

import com.java.admin.entity.user.UserEntity;
import com.java.admin.repository.user.UserRepository;
import com.java.admin.service.auth.UserDetailServiceImpl;
import com.java.admin.security.CustomAuthUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailServiceImpl userDetailsService;

    private UserEntity userEntity;

    private static final String VALID_EMAIL = "test@example.com";
    private static final String VALID_PASSWORD = "password123";
    private static final Long VALID_USER_ID = 1L;
    private static final String INVALID_EMAIL = "invalid@example.com";

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(VALID_USER_ID);
        userEntity.setEmail(VALID_EMAIL);
        userEntity.setPassword(VALID_PASSWORD);
        userEntity.setEnabled(true);
        userEntity.setAccountNonExpired(true);
        userEntity.setCredentialsNonExpired(true);
        userEntity.setAccountNonLocked(true);
        userEntity.setCreatedAt(OffsetDateTime.now());
        userEntity.setUpdatedAt(OffsetDateTime.now());
    }

    @Test
    void loadUserByUsername_shouldReturnCustomAuthUser_whenUserExists() {
        // Arrange
        when(userRepository.findUserEntityByEmail(VALID_EMAIL)).thenReturn(Optional.of(userEntity));

        // Act
        UserDetails result = userDetailsService.loadUserByUsername(VALID_EMAIL);

        // Assert
        assertNotNull(result);
        assertInstanceOf(CustomAuthUser.class, result);
        assertEquals(VALID_EMAIL, result.getUsername());
        assertEquals(VALID_PASSWORD, result.getPassword());
        assertTrue(result.isEnabled());
        assertTrue(result.isAccountNonExpired());
        assertTrue(result.isCredentialsNonExpired());
        assertTrue(result.isAccountNonLocked());

        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));

        CustomAuthUser customAuthUser = (CustomAuthUser) result;
        assertEquals(VALID_USER_ID, customAuthUser.getId());

        verify(userRepository).findUserEntityByEmail(VALID_EMAIL);
    }

    @Test
    void loadUserByUsername_shouldThrowUsernameNotFoundException_whenUserNotFound() {
        // Arrange
        when(userRepository.findUserEntityByEmail(INVALID_EMAIL)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(INVALID_EMAIL)
        );

        assertEquals("User not found with email: " + INVALID_EMAIL, exception.getMessage());
        verify(userRepository).findUserEntityByEmail(INVALID_EMAIL);
    }

    @Test
    void loadUserByUsername_shouldReturnUserWithCorrectAuthorities_whenUserExists() {
        // Arrange
        when(userRepository.findUserEntityByEmail(VALID_EMAIL)).thenReturn(Optional.of(userEntity));

        // Act
        UserDetails result = userDetailsService.loadUserByUsername(VALID_EMAIL);

        // Assert
        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());

        GrantedAuthority authority = authorities.iterator().next();
        assertEquals("ROLE_USER", authority.getAuthority());

        verify(userRepository).findUserEntityByEmail(VALID_EMAIL);
    }

    @Test
    void loadUserByUsername_shouldHandleDisabledUser_whenUserExists() {
        // Arrange
        userEntity.setEnabled(false);
        when(userRepository.findUserEntityByEmail(VALID_EMAIL)).thenReturn(Optional.of(userEntity));

        // Act
        UserDetails result = userDetailsService.loadUserByUsername(VALID_EMAIL);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEnabled());
        assertEquals(VALID_EMAIL, result.getUsername());
        verify(userRepository).findUserEntityByEmail(VALID_EMAIL);
    }

    @Test
    void loadUserByUsername_shouldHandleExpiredUser_whenUserExists() {
        // Arrange
        userEntity.setAccountNonExpired(false);
        when(userRepository.findUserEntityByEmail(VALID_EMAIL)).thenReturn(Optional.of(userEntity));

        // Act
        UserDetails result = userDetailsService.loadUserByUsername(VALID_EMAIL);

        // Assert
        assertNotNull(result);
        assertFalse(result.isAccountNonExpired());
        assertEquals(VALID_EMAIL, result.getUsername());
        verify(userRepository).findUserEntityByEmail(VALID_EMAIL);
    }

    @Test
    void loadUserByUsername_shouldHandleLockedUser_whenUserExists() {
        // Arrange
        userEntity.setAccountNonLocked(false);
        when(userRepository.findUserEntityByEmail(VALID_EMAIL)).thenReturn(Optional.of(userEntity));

        // Act
        UserDetails result = userDetailsService.loadUserByUsername(VALID_EMAIL);

        // Assert
        assertNotNull(result);
        assertFalse(result.isAccountNonLocked());
        assertEquals(VALID_EMAIL, result.getUsername());
        verify(userRepository).findUserEntityByEmail(VALID_EMAIL);
    }

    @Test
    void loadUserByUsername_shouldHandleExpiredCredentials_whenUserExists() {
        // Arrange
        userEntity.setCredentialsNonExpired(false);
        when(userRepository.findUserEntityByEmail(VALID_EMAIL)).thenReturn(Optional.of(userEntity));

        // Act
        UserDetails result = userDetailsService.loadUserByUsername(VALID_EMAIL);

        // Assert
        assertNotNull(result);
        assertFalse(result.isCredentialsNonExpired());
        assertEquals(VALID_EMAIL, result.getUsername());
        verify(userRepository).findUserEntityByEmail(VALID_EMAIL);
    }
}