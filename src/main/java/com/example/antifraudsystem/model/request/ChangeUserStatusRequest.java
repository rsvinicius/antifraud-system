package com.example.antifraudsystem.model.request;

import com.example.antifraudsystem.util.enums.UserStatus;
import com.example.antifraudsystem.util.validators.ValueOfEnum;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ChangeUserStatusRequest {
    @NotEmpty
    private String username;

    @ValueOfEnum(enumClass = UserStatus.class)
    private String operation;
}

