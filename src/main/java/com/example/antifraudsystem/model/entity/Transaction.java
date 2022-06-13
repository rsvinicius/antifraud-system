package com.example.antifraudsystem.model.entity;

import com.example.antifraudsystem.model.View;
import com.example.antifraudsystem.util.enums.WorldRegionCodes;
import com.example.antifraudsystem.util.validators.Amount;
import com.example.antifraudsystem.util.validators.CardNumber;
import com.example.antifraudsystem.util.validators.Ip;
import com.example.antifraudsystem.util.validators.ValueOfEnum;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction {
    @Transient
    private static long allowedLimit;

    @Transient
    private static long manualLimit;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.TransactionView.class)
    private long transactionId;

    @Amount
    @JsonView(View.TransactionView.class)
    private Long amount;

    @Ip
    @JsonView(View.TransactionView.class)
    private String ip;

    @CardNumber
    @JsonView(View.TransactionView.class)
    private String number;

    @ValueOfEnum(enumClass = WorldRegionCodes.class)
    @JsonView(View.TransactionView.class)
    private String region;

    @JsonView(View.TransactionView.class)
    private LocalDateTime date;

    @JsonView(View.TransactionView.class)
    private String result;

    @JsonView(View.TransactionView.class)
    private String feedback = "";

    public static long getAllowedLimit() {
        return allowedLimit;
    }

    public static void setAllowedLimit(long allowedLimit) {
        Transaction.allowedLimit = allowedLimit;
    }

    public static long getManualLimit() {
        return manualLimit;
    }

    public static void setManualLimit(long manualLimit) {
        Transaction.manualLimit = manualLimit;
    }
}

