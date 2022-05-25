package com.example.antifraudsystem.model.response;

import lombok.Data;

@Data
public class ChangeUserStatusResponse {
    public ChangeUserStatusResponse(String username, Boolean isUserLocked) {
        String status = isUserLocked ? "locked" : "unlocked";
        this.status = String.format("User %s %s!", username, status);
    }

    private String status;
}
