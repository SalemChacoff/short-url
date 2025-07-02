package com.java.admin.controller.user;

import com.java.admin.constant.ApiUserEndpoints;
import com.java.admin.usecase.user.IUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(ApiUserEndpoints.BASE_PATH)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User", description = "User management operations")
public class UserController {

    private final IUserService userService;

}
