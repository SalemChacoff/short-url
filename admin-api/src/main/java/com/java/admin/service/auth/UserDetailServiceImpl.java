package com.java.admin.service.auth;

import com.java.admin.config.CustomLogger;
import com.java.admin.entity.user.UserEntity;
import com.java.admin.repository.user.UserRepository;
import com.java.admin.security.CustomAuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findUserEntityByEmail(username).orElse(null);
        if (userEntity != null) {
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(authority);

            // Use the Email instead of the username
            return new CustomAuthUser(
                    userEntity.getId(),
                    userEntity.getEmail(),
                    userEntity.getPassword(),
                    userEntity.isEnabled(),
                    userEntity.isAccountNonExpired(),
                    userEntity.isCredentialsNonExpired(),
                    userEntity.isAccountNonLocked(),
                    authorities);
        } else {
            CustomLogger.logWarning(UserDetailServiceImpl.class, "User not found with email: " + username);
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
    }
}
