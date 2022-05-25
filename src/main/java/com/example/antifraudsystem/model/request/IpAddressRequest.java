package com.example.antifraudsystem.model.request;

import com.example.antifraudsystem.util.validators.Ip;
import lombok.Data;

@Data
public class IpAddressRequest {
    @Ip
    private String ip;
}
