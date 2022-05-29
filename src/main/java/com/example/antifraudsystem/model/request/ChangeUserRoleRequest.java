package com.example.antifraudsystem.model.request;

import com.example.antifraudsystem.util.enums.UserRole;
import com.example.antifraudsystem.util.validators.ValueOfEnum;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ChangeUserRoleRequest {
    @NotEmpty
    private String username;

    @ValueOfEnum(enumClass = UserRole.class)
    private String role;
}

