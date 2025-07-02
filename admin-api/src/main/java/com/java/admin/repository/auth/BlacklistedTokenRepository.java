package com.java.admin.repository.auth;

import com.java.admin.entity.auth.BlacklistedTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedTokenEntity, Long> {
    boolean existsByToken(String token);
    void deleteByExpiryAtBefore(OffsetDateTime expiryAtBefore);
}