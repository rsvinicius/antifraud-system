package com.example.antifraudsystem.model.request;

import com.example.antifraudsystem.util.validators.Amount;
import lombok.Data;

@Data
public class TransactionRequest {
    @Amount
    private Long amount;
}
