package com.example.antifraudsystem.model.response;

import lombok.Data;

@Data
public class DeleteUserResponse {
    private final String username;
    private String status = "Deleted successfully!";
}