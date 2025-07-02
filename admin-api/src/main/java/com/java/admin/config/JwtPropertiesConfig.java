package com.java.admin.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Validated
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JwtPropertiesConfig {
    private String jwtSecret;
    private long refreshTokenExpiration;
    private long jwtExpiration;
}
