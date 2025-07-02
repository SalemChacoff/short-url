package com.java.admin.entity.user;

import com.java.admin.entity.url.UrlEntity;
import com.java.admin.security.AuthProvider;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
import java.util.HashSet;
import java.util.Set;

@Table(name = "user_table")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -4251474067779675033L;

    // Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;
    @Column(name = "address")
    private String address;

    // Relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<UrlEntity> urls = new HashSet<>();
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    // Security Fields
    @Column(name = "is_active", nullable = false)
    private boolean isActive;
    @Column(name = "last_login", nullable = true)
    private OffsetDateTime lastLogin;
    @Column(name = "is_enabled", nullable = false)
    private boolean isEnabled;
    @Column(name = "is_account_non_expired", nullable = false)
    private boolean isAccountNonExpired;
    @Column(name = "is_credentials_non_expired", nullable = false)
    private boolean isCredentialsNonExpired;
    @Column(name = "is_account_non_locked", nullable = false)
    private boolean isAccountNonLocked;

    // Verification Fields
    @Column(name = "verification_token")
    private String verificationToken;
    @Column(name = "verification_code")
    private String verificationCode;
    @Column(name = "verification_token_expiry")
    private OffsetDateTime verificationTokenExpiry;
    @Column(name = "max_verification_code_attempts")
    private Integer maxVerificationCodeAttempts;

    // Reset Password Fields
    @Column(name = "reset_password_token")
    private String resetPasswordToken;
    @Column(name = "reset_password_code")
    private String resetPasswordCode;
    @Column(name = "reset_password_token_expiry")
    private OffsetDateTime resetPasswordTokenExpiry;
    @Column(name = "max_reset_password_attempts")
    private Integer maxResetPasswordAttempts;

    // Timestamps
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private OffsetDateTime updatedAt;
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    public void addUrl(UrlEntity url) {
        urls.add(url);
        url.setUser(this);
    }

    public void removeUrl(UrlEntity url) {
        urls.remove(url);
        url.setUser(null);
    }

}
