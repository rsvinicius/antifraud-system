package com.example.antifraudsystem.service;

import com.example.antifraudsystem.config.SecurityConfig;
import com.example.antifraudsystem.model.entity.User;
import com.example.antifraudsystem.model.request.ChangeUserRoleRequest;
import com.example.antifraudsystem.model.request.ChangeUserStatusRequest;
import com.example.antifraudsystem.model.response.ChangeUserStatusResponse;
import com.example.antifraudsystem.model.response.DeleteUserResponse;
import com.example.antifraudsystem.persistence.UserRepository;
import com.example.antifraudsystem.util.enums.UserRole;
import com.example.antifraudsystem.util.enums.UserStatus;
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
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        if (userRepository.count() == 0) {
            user.setRole(UserRole.ADMINISTRATOR);
            user.setIsAccountLocked(false);
        }

        user.setPassword(securityConfig.getEncoder().encode(user.getPassword()));
        user.setUsername(user.getUsername());

        return userRepository.save(user);
    }

    public DeleteUserResponse delete(String username) {
        User user = findUser(username);

        userRepository.delete(user);

        return new DeleteUserResponse(username);
    }

    public List<User> list() {
        return userRepository.findAll();
    }

    public User changeRole(ChangeUserRoleRequest changeUserRoleRequest) {
        User user = findUser(changeUserRoleRequest.getUsername());

        try {
            UserRole userRole = UserRole.valueOf(changeUserRoleRequest.getRole().toUpperCase());
            if ((userRole != UserRole.SUPPORT) && (userRole != UserRole.MERCHANT)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else if (userRole.equals(user.getRole())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }
            user.setRole(userRole);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return userRepository.save(user);
    }

    public ChangeUserStatusResponse changeStatus(ChangeUserStatusRequest changeUserStatusRequest) {
        User user = findUser(changeUserStatusRequest.getUsername());

        if (user.getRole() == UserRole.ADMINISTRATOR) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        try {
            UserStatus userStatus = UserStatus.valueOf(changeUserStatusRequest.getOperation().toUpperCase());
            user.setIsAccountLocked(userStatus == UserStatus.LOCK);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        userRepository.save(user);
        return new ChangeUserStatusResponse(user.getUsername(), user.getIsAccountLocked());
    }

    private User findUser(String username) {
        return Optional
                .ofNullable(userRepository.findUserByUsernameIgnoreCase(username))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}

