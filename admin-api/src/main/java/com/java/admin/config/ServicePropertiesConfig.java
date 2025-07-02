package com.java.admin.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "service")
@Validated
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ServicePropertiesConfig {

    private String baseHostPath;
    private String emailVerifyTemplateName;
    private String emailResetPasswordTemplateName;
    private Integer maxVerificationCodeAttempts;
    private Integer maxResetPasswordAttempts;
}
