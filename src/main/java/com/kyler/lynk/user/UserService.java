package com.kyler.lynk.user;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(final UserRepository userRepository, final UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserResponse> findAllUsersExceptSelf(Authentication connectedUser) {
        return userRepository.findAllUsersExceptSelf(connectedUser.getName())
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }
}
