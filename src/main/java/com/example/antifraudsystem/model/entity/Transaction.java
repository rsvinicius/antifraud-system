package com.example.antifraudsystem.model.entity;

import com.example.antifraudsystem.annotations.Amount;
import lombok.Data;

@Data
public class Transaction {
    @Amount
    private Long amount;
}
