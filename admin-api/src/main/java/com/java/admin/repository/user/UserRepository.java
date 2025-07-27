package com.java.admin.repository.user;

import com.java.admin.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findUserEntityById(Long id);
    Optional<UserEntity> findUserEntityByEmail(String email);
    boolean existsUserEntityByEmail(String email);
    Optional<UserEntity> findUserEntityByVerificationToken(String verificationToken);
    Optional<UserEntity> findUserEntityByResetPasswordToken(String resetPasswordToken);
}
