package com.java.admin.entity.url;

import com.java.admin.entity.user.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Table(name = "url_table")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UrlEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -1133836016600605686L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "original_url", nullable = false)
    private String originalUrl;
    @Column(name = "short_url", unique = true)
    private String shortUrl;
    @Column(name = "custom_alias")
    private String customAlias;
    @Column(name = "description")
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private UserEntity user;
    @Column(name = "click_count", nullable = false)
    private Long clickCount;
    @Column(name = "is_active", nullable = false)
    private boolean isActive;
    @Column(name = "valid_since", nullable = false)
    private OffsetDateTime validSince;
    @Column(name = "valid_until")
    private OffsetDateTime validUntil;
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private OffsetDateTime updatedAt;
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

}
