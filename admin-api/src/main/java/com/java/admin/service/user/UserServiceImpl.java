package com.java.admin.service.user;

import com.java.admin.mapper.user.UserMapper;
import com.java.admin.repository.user.UserRepository;
import com.java.admin.usecase.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
}