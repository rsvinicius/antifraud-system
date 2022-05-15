package com.example.antifraudsystem.service;

import com.example.antifraudsystem.config.SecurityConfig;
import com.example.antifraudsystem.model.entity.User;
import com.example.antifraudsystem.model.response.UserDeleteResponse;
import com.example.antifraudsystem.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SecurityConfig securityConfig;

    @Autowired
    public UserService(UserRepository userRepository, SecurityConfig securityConfig) {
        this.userRepository = userRepository;
        this.securityConfig = securityConfig;
    }

    public User register(User user) {
        if (userRepository.existsByUsername(user.getUsername().toLowerCase())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        user.setPassword(securityConfig.getEncoder().encode(user.getPassword()));
        user.setUsername(user.getUsername().toLowerCase());

        return userRepository.save(user);
    }

    public UserDeleteResponse delete(String username) {
        User user = Optional
                .ofNullable(userRepository.findUserByUsername(username.toLowerCase()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        userRepository.delete(user);

        return new UserDeleteResponse(username);
    }

    public List<User> list() {
        return userRepository.findAll();
    }
}

