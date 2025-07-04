package com.java.admin.repository.url;

import com.java.admin.entity.url.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<UrlEntity, Long>, JpaSpecificationExecutor<UrlEntity> {

    @Query("SELECT u FROM UrlEntity u WHERE u.user.id = :userId AND u.isDeleted = false")
    Optional<UrlEntity> findUrlEntitiesByUserAndDeletedIsFalseAndOriginalUrl(
            @Param("userId") Long userId,
            @Param("originalUrl") String originalUrl);

    @Query("SELECT u FROM UrlEntity u WHERE u.id = :urlId AND u.user.id = :userId AND u.isDeleted = false")
    Optional<UrlEntity> findByIdAndUserIdAndNotDeleted(
            @Param("urlId") Long urlId,
            @Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE UrlEntity u SET u.isActive = :isActive WHERE u.id = :urlId AND u.user.id = :userId AND u.isDeleted = false")
    int toggleUrlStatus(
            @Param("urlId") Long urlId,
            @Param("userId") Long userId,
            @Param("isActive") boolean isActive);

    @Modifying
    @Transactional
    @Query("UPDATE UrlEntity u SET u.customAlias = :customAlias, u.originalUrl = :originalUrl, " +
            "u.description = :description, u.validSince = :validSince, u.validUntil = :validUntil, " +
            "u.isActive = :isActive WHERE u.id = :urlId AND u.user.id = :userId AND u.isDeleted = false")
    int updateUrl(
            @Param("urlId") Long urlId,
            @Param("userId") Long userId,
            @Param("customAlias") String customAlias,
            @Param("originalUrl") String originalUrl,
            @Param("description") String description,
            @Param("validSince") OffsetDateTime validSince,
            @Param("validUntil") OffsetDateTime validUntil,
            @Param("isActive") boolean isActive);

    @Modifying
    @Transactional
    @Query("UPDATE UrlEntity u SET u.isDeleted = true WHERE u.id = :urlId AND u.user.id = :userId AND u.isDeleted = false")
    int softDeleteUrl(
            @Param("urlId") Long urlId,
            @Param("userId") Long userId);
}
