package com.java.admin.repository.url;

import com.java.admin.entity.url.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<UrlEntity, Long>, JpaSpecificationExecutor<UrlEntity> {

    @Query("SELECT u FROM UrlEntity u WHERE u.user.id = :userId AND u.isDeleted = false")
    Optional<UrlEntity> findUrlEntitiesByUserAndDeletedIsFalseAndOriginalUrl(
            @Param("userId") Long userId,
            @Param("originalUrl") String originalUrl);
}
