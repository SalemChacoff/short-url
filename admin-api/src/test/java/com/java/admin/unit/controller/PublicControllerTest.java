package com.java.admin.unit.controller;

import com.java.admin.controller.PublicController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PublicControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private PublicController publicController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(publicController).build();
    }

    @Test
    @DisplayName("Should return UP status when health endpoint is called")
    void health_shouldReturnUpStatus() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/public/health")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.message").value("Application is running successfully"));
    }

    @Test
    @DisplayName("Should return application info when info endpoint is called")
    void info_shouldReturnApplicationInfo() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/public/info")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.application").exists())
                .andExpect(jsonPath("$.application.name").value("Admin Service"))
                .andExpect(jsonPath("$.application.version").value("1.0.0"))
                .andExpect(jsonPath("$.application.environment").exists());
    }

    @Test
    @DisplayName("Should handle invalid endpoints with 404")
    void invalidEndpoint_shouldReturn404() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/public/nonexistent")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}