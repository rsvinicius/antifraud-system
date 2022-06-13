package com.example.antifraudsystem.controller;

import com.example.antifraudsystem.model.View;
import com.example.antifraudsystem.model.entity.User;
import com.example.antifraudsystem.model.request.ChangeUserRoleRequest;
import com.example.antifraudsystem.model.request.ChangeUserStatusRequest;
import com.example.antifraudsystem.model.response.ChangeUserStatusResponse;
import com.example.antifraudsystem.model.response.DeleteUserResponse;
import com.example.antifraudsystem.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@Validated
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    @JsonView(View.UserView.class)
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@Valid @RequestBody User user) {
        return userService.register(user);
    }

    @DeleteMapping("/user/{username}")
    public DeleteUserResponse delete(@NotBlank @PathVariable String username) {
        return userService.delete(username);
    }

    @GetMapping("/list")
    @JsonView(View.UserView.class)
    public List<User> listAllUsers() {
        return userService.findAllUsers();
    }

    @PutMapping("/role")
    @JsonView(View.UserView.class)
    public User changeRole(@Valid @RequestBody ChangeUserRoleRequest changeUserRoleRequest) {
        return userService.changeRole(changeUserRoleRequest);
    }

    @PutMapping("/access")
    public ChangeUserStatusResponse changeStatus(@Valid @RequestBody ChangeUserStatusRequest changeUserStatusRequest) {
        return userService.changeStatus(changeUserStatusRequest);
    }
}
