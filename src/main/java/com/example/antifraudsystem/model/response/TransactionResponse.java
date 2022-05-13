package com.example.antifraudsystem.model.response;

import com.example.antifraudsystem.util.enums.TransactionResultType;
import lombok.Data;

@Data
public class TransactionResponse {
    private final TransactionResultType result;
}
