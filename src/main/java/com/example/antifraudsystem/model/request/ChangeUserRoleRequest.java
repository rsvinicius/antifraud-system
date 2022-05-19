package com.example.antifraudsystem.model.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ChangeUserRoleRequest {
    @NotEmpty
    private String username;

    @NotEmpty
    private String role;
}

