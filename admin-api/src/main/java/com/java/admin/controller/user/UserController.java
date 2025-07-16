package com.java.admin.controller.user;

import com.java.admin.constant.ApiUserEndpoints;
import com.java.admin.usecase.user.IUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(ApiUserEndpoints.BASE_PATH)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User", description = "User management operations")
public class UserController {

    private final IUserService userService;

    @GetMapping(value = "/info", produces = "application/json")
    public String getUserInfo() {
        // This is a placeholder for user info retrieval logic
        log.info("Retrieving user information");
        return "User information retrieved successfully";
    }

    @PostMapping(value = "/profile", consumes = "application/json", produces = "application/json")
    public String updateUserProfile() {
        // This is a placeholder for user profile update logic
        log.info("Updating user profile");
        return "User profile updated successfully";
    }

}
