package com.example.antifraudsystem.service;

import com.example.antifraudsystem.model.request.TransactionRequest;
import com.example.antifraudsystem.model.response.TransactionResponse;
import com.example.antifraudsystem.util.enums.TransactionResultType;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    public TransactionResponse transaction(TransactionRequest transactionRequest) {
        TransactionResultType transactionResultType;
        Long amount = transactionRequest.getAmount();

        if (amount <= 200) {
            transactionResultType = TransactionResultType.ALLOWED;
        } else if (amount > 1500) {
            transactionResultType = TransactionResultType.PROHIBITED;
        } else {
            transactionResultType = TransactionResultType.MANUAL_PROCESSING;
        }

        return new TransactionResponse(transactionResultType);
    }
}

