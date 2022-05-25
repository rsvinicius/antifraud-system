package com.example.antifraudsystem.model.response;

import lombok.Data;

@Data
public class DeleteIpAddressResponse {
    public DeleteIpAddressResponse(String ipAddress) {
        this.status = String.format("IP %s successfully removed!", ipAddress);
    }

    private String status;
}
