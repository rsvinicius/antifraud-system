package com.example.antifraudsystem.model.entity;

import com.example.antifraudsystem.util.enums.WorldRegionCodes;
import com.example.antifraudsystem.util.validators.Amount;
import com.example.antifraudsystem.util.validators.CardNumber;
import com.example.antifraudsystem.util.validators.Ip;
import com.example.antifraudsystem.util.validators.ValueOfEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction extends IdentityId {
    @Amount
    private Long amount;

    @Ip
    private String ip;

    @CardNumber
    private String number;

    @ValueOfEnum(enumClass = WorldRegionCodes.class)
    private String region;

    private LocalDateTime date;
}

