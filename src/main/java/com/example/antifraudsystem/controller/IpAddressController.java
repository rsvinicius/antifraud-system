package com.example.antifraudsystem.controller;

import com.example.antifraudsystem.model.entity.IpAddress;
import com.example.antifraudsystem.model.request.IpAddressRequest;
import com.example.antifraudsystem.model.response.DeleteIpAddressResponse;
import com.example.antifraudsystem.service.IpAddressService;
import com.example.antifraudsystem.util.validators.Ip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/antifraud/suspicious-ip")
@Validated
public class IpAddressController {
    private final IpAddressService ipAddressService;

    @Autowired
    public IpAddressController(IpAddressService ipAddressService) {
        this.ipAddressService = ipAddressService;
    }

    @PostMapping
    public IpAddress saveSuspiciousIpAddress(@Valid @RequestBody IpAddressRequest ipAddressRequest) {
        return ipAddressService.saveSuspiciousIpAddress(ipAddressRequest);
    }

    @DeleteMapping("/{ipAddress}")
    public DeleteIpAddressResponse deleteIpAddress(@Ip @PathVariable String ipAddress) {
        return ipAddressService.deleteIpAddress(ipAddress);
    }

    @GetMapping
    public List<IpAddress> listIpAddresses() {
        return ipAddressService.listIpAddress();
    }
}
