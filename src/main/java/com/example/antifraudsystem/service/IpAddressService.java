package com.example.antifraudsystem.service;


import com.example.antifraudsystem.model.entity.IpAddress;
import com.example.antifraudsystem.model.request.IpAddressRequest;
import com.example.antifraudsystem.model.response.DeleteIpAddressResponse;
import com.example.antifraudsystem.repository.IpAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class IpAddressService {
    private final IpAddressRepository ipAddressRepository;

    @Autowired
    public IpAddressService(IpAddressRepository ipAddressRepository) {
        this.ipAddressRepository = ipAddressRepository;
    }

    public IpAddress saveSuspiciousIpAddress(IpAddressRequest ipAddressRequest) {
        if (ipAddressRepository.existsByIp(ipAddressRequest.getIp())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        return ipAddressRepository.save(new IpAddress(ipAddressRequest.getIp()));
    }

    public DeleteIpAddressResponse deleteIpAddress(String ipAddress) {
        IpAddress ip = findIpAddress(ipAddress);

        ipAddressRepository.delete(ip);

        return new DeleteIpAddressResponse(ipAddress);
    }

    public List<IpAddress> listIpAddress() {
        return ipAddressRepository.findAll();
    }

    private IpAddress findIpAddress(String ipAddress) {
        return Optional
                .ofNullable(ipAddressRepository.findByIp(ipAddress))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
