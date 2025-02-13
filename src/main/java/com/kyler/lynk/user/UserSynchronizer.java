package com.kyler.lynk.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserSynchronizer {
    private static final Logger log = LoggerFactory.getLogger(UserSynchronizer.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserSynchronizer(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public void synchronizeWithIdp(Jwt token) {
        log.info("Synchronizing with idp");
        getUserEmail(token).ifPresent(userEmail -> {
            Optional<User> optUser = userRepository.findUserByEmail(userEmail);
            User user = userMapper.fromTokenAttributes(token.getClaims());
            optUser.ifPresent(value -> user.setId(value.getId()));
            userRepository.save(user);
        });
    }

    private Optional<String> getUserEmail(Jwt token) {
        Map<String, Object> claims = token.getClaims();

        if (!claims.containsKey("email")) {
            return Optional.empty();
        }

        return Optional.of(claims.get("email").toString());
    }
}
