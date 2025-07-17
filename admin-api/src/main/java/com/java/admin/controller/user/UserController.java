package com.java.admin.controller.user;

import com.java.admin.config.CustomLogger;
import com.java.admin.constant.ApiUserEndpoints;
import com.java.admin.dto.ApiResponseDto;
import com.java.admin.dto.user.request.UpdateUserRequestDto;
import com.java.admin.usecase.user.IUserService;
import com.java.admin.util.CustomAuthUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(ApiUserEndpoints.BASE_PATH)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User", description = "User management operations")
public class UserController {

    private final IUserService userService;

    @GetMapping(value = ApiUserEndpoints.PROFILE_PATH, produces = "application/json")
    public ResponseEntity<ApiResponseDto> getUserInfo(Authentication authentication) {
        CustomAuthUser getCurrentUserId = (CustomAuthUser) authentication.getPrincipal();
        CustomLogger.logInfo(UserController.class, "Fetching user info for user ID: " + getCurrentUserId.getId());
        return ResponseEntity.ok(
                new ApiResponseDto(
                        null,
                        true,
                        userService.getUserById(getCurrentUserId.getId())
                ));
    }

    @PostMapping(value = ApiUserEndpoints.PROFILE_PATH, consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponseDto> updateUserProfile(@Valid @RequestBody UpdateUserRequestDto updateUserRequestDto,
                                                            Authentication authentication) {
        CustomAuthUser getCurrentUserId = (CustomAuthUser) authentication.getPrincipal();
        CustomLogger.logInfo(UserController.class, "Fetching user info for user ID: " + getCurrentUserId.getId());
        return ResponseEntity.ok(
                new ApiResponseDto(
                        null,
                        true,
                        userService.updateUser(getCurrentUserId.getId(), updateUserRequestDto)
                ));
    }

}
