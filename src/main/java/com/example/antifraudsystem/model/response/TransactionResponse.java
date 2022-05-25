package com.example.antifraudsystem.model.response;

import com.example.antifraudsystem.util.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private TransactionStatus result;
    private String info;
}
