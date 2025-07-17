package com.java.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/public")
public class PublicController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "message", "Application is running successfully"
        ));
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> info = new HashMap<>();

        // Información de la aplicación
        info.put("application", Map.of(
                "name", "Admin Service",
                "version", "1.0.0",
                "environment", System.getProperty("spring.profiles.active", "default")
        ));

        // Información del sistema
        info.put("system", Map.of(
                "timezone", ZoneId.systemDefault().toString(),
                "timestamp", Instant.now().toString(),
                "uptime", ManagementFactory.getRuntimeMXBean().getUptime() + "ms",
                "java_version", System.getProperty("java.version"),
                "os", System.getProperty("os.name") + " " + System.getProperty("os.version")
        ));

        // Variables de entorno (filtradas por seguridad)
        Map<String, String> envVars = new HashMap<>();
        envVars.put("SPRING_PROFILES_ACTIVE", System.getenv("SPRING_PROFILES_ACTIVE"));
        envVars.put("DATABASE_URL", maskSensitiveInfo(System.getenv("DATABASE_URL")));
        envVars.put("REDIS_URL", maskSensitiveInfo(System.getenv("REDIS_URL")));
        info.put("environment_variables", envVars);

        // Información de memoria
        Runtime runtime = Runtime.getRuntime();
        info.put("memory", Map.of(
                "total", runtime.totalMemory() / 1024 / 1024 + "MB",
                "free", runtime.freeMemory() / 1024 / 1024 + "MB",
                "used", (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024 + "MB"
        ));

        return ResponseEntity.ok(info);
    }

    private String maskSensitiveInfo(String value) {
        if (value == null || value.length() <= 8) return "***";
        return value.substring(0, 4) + "***" + value.substring(value.length() - 4);
    }
}
