package com.example.antifraudsystem.model.request;

import com.example.antifraudsystem.util.validators.Amount;
import com.example.antifraudsystem.util.validators.CardNumber;
import com.example.antifraudsystem.util.validators.Ip;
import lombok.Data;

@Data
public class TransactionRequest {
    @Amount
    private Long amount;

    @Ip
    private String ip;

    @CardNumber
    private String number;
}

