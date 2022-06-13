package com.example.antifraudsystem.repository;

import com.example.antifraudsystem.model.entity.IpAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpAddressRepository extends JpaRepository<IpAddress, Long> {
    IpAddress findByIp(String ipAddress);
    Boolean existsByIp(String ipAddress);
}
