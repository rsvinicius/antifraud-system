package com.example.antifraudsystem.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class IpAddress extends IdentityId {
    public IpAddress(String ip) {
        this.ip = ip;
    }

    private String ip;
}